#!/bin/bash

TIMEOUT_MINUTES=180

echo "=================================="
echo "Session de développement Corporate Fleet Manager"
echo "Timeout auto-stop : $TIMEOUT_MINUTES minutes"
echo "=================================="

# 1. Vérifie si les conteneurs tournent, sinon les démarre
echo "Vérification des conteneurs..."

# Récupère les IDs des conteneurs running
WILDFLY_RUNNING=$(docker ps -q -f name=wildfly-local)
ORACLE_RUNNING=$(docker ps -q -f name=oracle-local)

if [ -z "$WILDFLY_RUNNING" ] || [ -z "$ORACLE_RUNNING" ]; then
    echo "Un ou plusieurs conteneurs ne tournent pas → démarrage..."
    echo "Démarrage des conteneurs (timeout augmenté)..."
    export COMPOSE_HTTP_TIMEOUT=120
    docker-compose up -d

    # Attends que WildFly soit vraiment prêt
    echo "Attente du démarrage complet de WildFly..."
    sleep 30

    # Vérifie que WildFly répond
    MAX_RETRIES=10
    RETRY_COUNT=0

    while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
        if docker exec wildfly-local //opt/jboss/wildfly/bin/jboss-cli.sh --connect --command=":read-attribute(name=server-state)" 2>/dev/null | grep -q "running"; then
            echo "WildFly est opérationnel"
            break
        fi

        echo "Attente... ($((RETRY_COUNT+1))/$MAX_RETRIES)"
        sleep 10
        RETRY_COUNT=$((RETRY_COUNT+1))
    done

    if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
        echo "ERREUR : WildFly ne démarre pas"
        docker logs wildfly-local --tail 50
        exit 1
    fi

    echo "Conteneurs lancés"
else
    echo "Conteneurs déjà en cours d'exécution"
fi

# 2. Compilation
echo "Compilation du projet..."
mvn clean package -q
if [ $? -ne 0 ]; then
    echo "Échec de la compilation Maven"
    exit 1
fi
echo "Compilation réussie"

# 3. Copie du nouveau WAR
echo "Copie du nouveau WAR..."
docker cp target/corporate-fleet-manager.war wildfly-local:/opt/jboss/wildfly/standalone/deployments/ROOT.war
if [ $? -ne 0 ]; then
    echo "Échec de la copie du WAR (container arrêté ?)"
    exit 1
fi

# 4. Redéploiement propre avec reload
echo "Redéploiement propre via reload..."
docker exec wildfly-local //opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="reload"
if [ $? -ne 0 ]; then
    echo "Échec du reload CLI"
    echo "Tentative alternative de redeploy..."
    docker exec wildfly-local //opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="deploy /opt/jboss/wildfly/standalone/deployments/ROOT.war --force"
fi

sleep 10

echo ""
echo "=== Logs du redeploy ==="
docker logs wildfly-local --tail 50 | grep -E "deployed|WFLYSRV0027|WFLYUT0021|WFLYSRV0016|ERROR|Exception"

echo ""
echo "Application disponible sur http://localhost:8180"
echo ""

# 5. Timer auto-stop (réinitialisé à chaque lancement)
(
    sleep $(($TIMEOUT_MINUTES * 60))
    echo ""
    echo "Timeout de $TIMEOUT_MINUTES minutes atteint – arrêt des conteneurs..."
    docker-compose down
    echo "Conteneurs arrêtés automatiquement."
) &

echo "Session active – relance le script à chaque modification pour redéployer."