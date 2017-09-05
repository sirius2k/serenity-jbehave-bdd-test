pipeline {
    agent any

    tools {
        maven 'apache-maven-3.5.0'
    }

    environment {
        REPORT_NAME = "Serenity report ${BUILD_NUMBER}"

    }

    parameters {
        string(
            name: 'activeProfile',
            defaultValue: 'local',
            description: 'Active profile'
        )
    }

    stages {
        stage('Preparation') {
            steps {
                echo "Current workspace : ${workspace}"

                script {
                    if (isUnix()) {
                        module = load "${workspace}/Jenkinsfile_module.groovy"
                    } else {
                        module = load "..\\workspace\\Jenkinsfile_module.groovy"
                    }
                }

                echo "module = ${module}"

                script {
                    def causes = currentBuild.rawBuild.getCauses()
                    for (cause in causes) {
                        module.printCausesRecursively(cause)
                    }
                }
            }
        }

        stage('Test') {
            steps {
                ansiColor('xterm') {
                    sh "mvn -P ${activeProfile} clean verify"
                }
            }
        }

        stage('PublishHTML') {
            steps {
                publishHTML(target: [
                        reportName           : REPORT_NAME,
                        reportDir            : 'target/site/serenity',
                        reportFiles          : 'index.html',
                        keepAll              : true,
                        alwaysLinkToLastBuild: true,
                        allowMission         : false
                ])
            }
        }
    }
}