pipeline {
    agent {label 'jenkins_agent'}
    tools {
        maven 'maven3'
        //gradle 'gradle8'
    }
    stages {
        stage("Setup Parameters") {
            steps {
                script {
                    properties([
                        parameters([
                            string(
                                defaultValue: 'none',
                                description: 'version',
                                name: 'VERSION',
                                trim: true
                            )
                        ])
                    ])
                }
            }
        }
        stage('test Jenkinsfile') {
            steps {
                script {
                    sh 'gradle test'
                }
            }
        }
        stage('checkout project repo') {
            steps {
                git url: 'https://github.com/dzmitrydan/aircompany.git'
            }
        }
        stage('execute unit tests') {
            steps {
                script {
                    sh 'mvn -f Java/pom.xml clean test'
                }
            }
        }
        stage('prepare build artifact') {
            steps {
                script {
                    sh 'mvn -f Java/pom.xml install'
                }
            }
        }
        stage('push into Artifactory') {
            steps {
                script {
                    server = Artifactory.server 'artifactory'
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "**/target/*SNAPSHOT.jar",
                                "target": "aircompany/${VERSION}/"
                            }
                        ]}"""
                    server.upload(uploadSpec)
                }
            }
        }
        stage('archive artifacts') {
            steps {
                script {
                   def date = new Date()
                   def data = "app version ${VERSION}\nhttps://blesstask.jfrog.io/ui/repos/tree/General/aircompany/${VERSION}\n" + date
                   writeFile(file: 'ARTIFACTORY.txt', text: data)
                   sh "ls -l"
               }
                archiveArtifacts allowEmptyArchive: true,
                artifacts: 'ARTIFACTORY.txt, **/target/*SNAPSHOT.jar', '**/target/surefire-reports/**',
                followSymlinks: false
            }
        }
    }
}