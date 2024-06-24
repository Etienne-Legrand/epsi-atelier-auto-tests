pipeline {
    agent any
    tools {
        jdk 'JDK-21'
        // Install the Maven version configured in Jenkins
        maven 'M-3.8.1'
    }
    environment {
        // Define any environment variables if needed
        MAVEN_HOME = tool 'M-3.8.1'
    }
    stages {
        stage('Checkout') {
            steps {
                // Clone the repository and checkout the main branch
                git branch: 'main', url: 'https://github.com/Etienne-Legrand/epsi-atelier-auto-tests.git'
            }
        }
        stage('Build') {
            steps {
                // Compile the application located in exercice5
                sh "${MAVEN_HOME}/bin/mvn -f exercice5/pom.xml clean compile"
            }
        }
        stage('Unit Tests') {
            steps {
                // Run unit tests located in exercice5
                sh "${MAVEN_HOME}/bin/mvn -f exercice5/pom.xml test"
            }
        }
        stage('Integration Tests') {
            steps {
                // Run integration tests located in exercice5
                sh "${MAVEN_HOME}/bin/mvn -f exercice5/pom.xml verify"
            }
        }
    }
    post {
        success {
            // Send Discord message on success
            sh '''
                curl -H "Content-Type: application/json" \
                -X POST \
                -d '{"content": "SUCCESS: Build succeeded."}' \
                ${DISCORD_WEBHOOK_URL}
            '''
        }
        failure {
            // Send Discord message on failure
            sh '''
                curl -H "Content-Type: application/json" \
                -X POST \
                -d '{"content": "FAILURE: Build failed."}' \
                ${DISCORD_WEBHOOK_URL}
            '''
        }
    }
}