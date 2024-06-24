# Atelier auto test

## Exercice 4 (JUnit Calculatrice Java)

## Exercice 5 (JUnit Java Spring Boot)

Prérequis : Java 21 et maven 3

Démarrer le projet Spring Boot : `mvn spring-boot:run`

Démarrer les tests : `mvn test`

## Exercice 6 (Selenium)

On a utilisé Jupyter Notebook.

## Exercice 7 (JMeter)

## Exercice 8 (Java Jenkins)

### Documentation

### Prérequis :

- Jenkins installé et configuré.
- Accès administrateur à Jenkins.
- Un repository GitHub avec votre application Java.
- Java et Maven installés sur les agents de build.
  
### Étape 1 : Configuration de Jenkins

#### 1. Installer les plugins nécessaires
   
- Connectez-vous à Jenkins.
- Allez dans "Manage Jenkins" > "Manage Plugins".
- Installez les plugins suivants :
  - Pipeline Plugin
  - GitHub Integration Plugin
  - Maven Integration Plugin
    
#### 3. Configurer JDK et Maven
   
Allez dans "Manage Jenkins" > "Global Tool Configuration".
Sous "JDK installations", cliquez sur "Add JDK". Nommez-le "JDK 21" et renseignez le chemin d'installation (JAVA_HOME).
Sous "Maven installations", cliquez sur "Add Maven". Nommez-le "Maven 3.8.1" et renseignez le chemin d'installation (MAVEN_HOME).

### Étape 2 : Configuration des Webhooks GitHub

Créer un Webhook sur GitHub :

1- Allez dans votre repository GitHub.

2- Cliquez sur "Settings" > "Webhooks" > "Add webhook".

3- Renseignez l'URL du webhook avec l'URL de votre Jenkins suivi de /github-webhook/, par exemple http://your-jenkins-url/github-webhook/.

4- Sélectionnez application/json comme "Content type".

5- Sélectionnez les événements à surveiller (par exemple, Just the push event).

6- Cliquez sur "Add webhook".

7- Configurer le Webhook dans Jenkins :

### Étape 3 : Configuration du Pipeline Jenkins

1- Allez dans "New Item" > "Pipeline".

![image](https://github.com/Etienne-Legrand/epsi-atelier-auto-tests/assets/93017364/9b60343e-44ce-48f7-b0e9-554dfe0c3596)

2- Donnez un nom à votre pipeline et cliquez sur "OK".

![image](https://github.com/Etienne-Legrand/epsi-atelier-auto-tests/assets/93017364/30f4a1c1-4972-42a4-8164-4181ccd45769)

3- Configurer le pipeline :
- Dans la configuration du pipeline, sous "Pipeline", choisissez "Pipeline script".
- Sélectionnez "Git" et renseignez l'URL de votre repository.
- Spécifiez la branche à construire (par exemple, */main).
  
![image](https://github.com/Etienne-Legrand/epsi-atelier-auto-tests/assets/93017364/9be470e9-8a49-4e69-9118-39dd69ee71df)

### Étape 4 : Configuration des Agents de Build

1- Configurer les agents de build (si nécessaire) :

2- Allez dans "Manage Jenkins" > "Manage Nodes and Clouds".

3- Ajoutez un nouvel agent si nécessaire, spécifiez les étiquettes et configurez les outils nécessaires (JDK, Maven).

### Étape 5 : Notifications de Résultats

1- Configurer les notifications par channel discord :

2- Création serveur discord

3-  dans la pipeline:       success {
            // Send Discord message on success
            sh '''
                curl -H "Content-Type: application/json" \
                -X POST \
                -d '{"content": "SUCCESS: Build ${env.BUILD_NUMBER} succeeded."}' \
                ${DISCORD_WEBHOOK_URL}

                ![image](https://github.com/Etienne-Legrand/epsi-atelier-auto-tests/assets/93017364/452da218-1275-4064-a72d-25d6cdc2144e)

