#!/bin/bash

TIMEOUT_MINUTES=30

echo "=================================="
echo "Session de développement Corporate Fleet Manager"
echo "Timeout auto-stop : $TIMEOUT_MINUTES minutes"
echo "=================================="

# 1. Vérifie si les conteneurs tournent, sinon les démarre
echo "Vérification des conteneurs..."
if ! docker ps -q -f name=wildfly-local > /dev/null || ! docker ps -q -f name=oracle-local > /dev/null; then
    echo "Démarrage des conteneurs..."
    docker-compose up -d
    sleep 10  # attente que tout soit prêt
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
    echo "Échec de la copie du WAR"
    exit 1
fi

# 4. Redéploiement propre avec reload
echo "Redéploiement propre via reload..."
docker exec wildfly-local //opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="reload"
if [ $? -ne 0 ]; then
    echo "Échec du reload CLI"
    exit 1
fi

sleep 10  # temps pour que le reload finisse

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
) &

echo "Session active – relance le script à chaque modification."