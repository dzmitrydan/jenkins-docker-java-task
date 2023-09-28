pipeline {
    agent {label 'jenkins_agent'}
    tools {
        maven 'maven3'
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
        stage('Pipeline Quality Gates') {
            steps {
                script {
                    sh './gradlew clean check'
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
                artifacts: 'ARTIFACTORY.txt, **/target/*SNAPSHOT.jar, **/test-results/test/TEST-*.xml',
                followSymlinks: false

                buildAddUrl(title: 'Deploy to PROD', url: "/job/app-deploy/parambuild/?env=prod&version='123'")
                addDeployToDashboard(env: 'ENV', buildNumber: '123')
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            recordIssues(tools: [codeNarc(pattern: '**/codenarc/test.xml', reportEncoding: 'UTF-8')])
        }
    }
}