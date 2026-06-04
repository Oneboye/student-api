pipeline { 
    agent any

    // Outils déclarés dans l'interface Jenkins (Manage Jenkins > Tools)
    tools { 
        maven 'Maven-3.9' 
        jdk   'JDK-17'
    }

    stages {
        // Stage 1 : Récupération du code depuis le dépôt Git
        stage('Checkout') { 
            steps { 
                checkout scm 
                echo "Build #${env.BUILD_NUMBER} | Branche : ${env.BRANCH_NAME}"
            }
        }

        // Stage 2 : Compilation rapide de l'application (sans exécuter les tests)
        stage('Build') { 
            steps { 
                bat 'mvn clean install -Dcheckstyle.skip=true'
            }
        }

        // Stage 3 : Exécution des tests unitaires JUnit
        stage('Tests Unitaires') { 
            steps { 
                bat 'mvn test'
            }
            post { 
                always { 
                    // Publication des rapports XML de tests sur l'interface Jenkins
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }

        // Stage 4 : Analyse de la couverture de code avec JaCoCo (Seuil 70 %)
        stage('Couverture') { 
            steps { 
                bat 'mvn verify'
            }
            post { 
                always { 
                    // Génération et affichage du graphique de couverture dans Jenkins
                    jacoco( 
                        execPattern: 'target/*.exec',
                        classPattern: 'target/classes', 
                        sourcePattern: 'src/main/java' 
                    )
                }
            }
        }

        // Stage 5 : Archivage du fichier JAR pour un téléchargement ultérieur
        stage('Archivage') { 
            steps { 
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    // Actions globales exécutées selon le statut final de la chaîne
    post { 
        success { 
            echo 'Pipeline réussi avec succès !'      
        } 
        failure { 
            echo 'Pipeline échoué -- consultez les logs.' 
        }
    }
}