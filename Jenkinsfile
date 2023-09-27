pipeline {
    agent {label 'jenkins_agent'}
    tools {
        maven 'maven3'
        gradle 'gradle8'
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
        stage('Test Jenkinsfile') {
            steps {
                script {
                    sh 'gradle clean test'
                }
            }
        }
        stage('Checkout Project Repo') {
            steps {
                git url: 'https://github.com/dzmitrydan/aircompany.git'
            }
        }
        stage('Execute Unit Tests') {
            steps {
                script {
                    sh 'mvn -f Java/pom.xml clean test'
                }
            }
        }
        stage('Prepare Build Artifacts') {
            steps {
                script {
                    sh 'mvn -f Java/pom.xml install'
                }
            }
        }
        stage('Push into Artifactory') {
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
        stage('Archive Artifacts') {
            steps {
                script {
                   def date = new Date()
                   def data = "app version ${VERSION}\nhttps://blesstask.jfrog.io/ui/repos/tree/General/aircompany/${VERSION}\n" + date
                   writeFile(file: 'ARTIFACTORY.txt', text: data)
                   sh "ls -l"
               }
                archiveArtifacts allowEmptyArchive: true,
                artifacts: 'ARTIFACTORY.txt, **/target/*SNAPSHOT.jar, **/test-results/TEST-JenkinsFileTest.xml, **/codenarc/test.html',
                followSymlinks: false
            }
        }
    }
}