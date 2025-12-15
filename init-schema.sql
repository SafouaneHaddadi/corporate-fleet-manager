-- change de container pour accéder à la PDB (base utilisateur)
ALTER SESSION SET CONTAINER = FREEPDB1;

-- crée le user ORA53 avec mot de passe oracle (exactement comme à Condorcet)
CREATE USER ORA53 IDENTIFIED BY oracle;

-- donne tous les droits nécessaires
GRANT CONNECT, RESOURCE, DBA TO ORA53;

-- autorise le user à utiliser tout l'espace disque
ALTER USER ORA53 DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS;