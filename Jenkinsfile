pipeline { 
    agent any 
    
    tools { 
        maven 'Maven-3.9' 
        jdk   'JDK-17'
    }

    stages {
        // Stage 1 : Récupération du code
        stage('Checkout') { 
            steps { 
                checkout scm 
                echo "Build #${env.BUILD_NUMBER}" 
                echo "Branche : ${env.BRANCH_NAME}"
            }
        }
        // Stage 2 : Compilation de l'application
        stage('Build') { 
            steps { 
                bat 'mvn clean package -DskipTests'
            }
        }

        // Stage 3 : Le nouveau stage demandé au point 6.1 (Analyse Checkstyle)
        stage('Lint') {
            steps { 
                bat 'mvn checkstyle:check' 
            }
        }

        // Stage 4 : Tests Unitaires et génération des rapports de tests
        stage('Tests Unitaires') { 
            steps { 
                bat 'mvn test'
            }
            post { 
                always { 
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }

        // Stage 5 : Analyse de la couverture de code avec JaCoCo
        stage('Couverture') { 
            steps { 
                bat 'mvn verify'
            }
            post { 
                always { 
                    jacoco( 
                        execPattern: 'target/*.exec',
                        classPattern: 'target/classes', 
                        sourcePattern: 'src/main/java'
                    )
                }
            }
        }

        // Stage 6 : Archivage du fichier JAR généré
        stage('Archivage') { 
            steps { 
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    // Actions globales de fin de Pipeline
    post { 
        success { 
            echo 'Pipeline reussi avec succes !' 
        } 
        failure { 
            echo 'Pipeline echoue -- verifiez les logs.' 
        }
    }
}