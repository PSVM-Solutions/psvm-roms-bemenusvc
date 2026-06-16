pipeline {
    agent any

    tools {
        // Defines the JDK tool configured globally in your Jenkins instance (needs to match Java 21)
        jdk 'jdk-21'
    }

    environment {
        // Credentials ID for GitHub Packages in Jenkins. 
        // This should be a "Username with password" credential containing your GitHub username and Personal Access Token (PAT).
        GITHUB_CREDS = credentials('github-packages-token')
        
        // Image name for containerization (optional)
        IMAGE_NAME = 'menu-service'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Maven Settings') {
            steps {
                echo 'Generating temporary settings.xml for GitHub Packages authentication...'
                // Create a temporary maven settings.xml to authenticate with GitHub Packages to pull the commonModels dependency.
                writeFile file: 'tmp-settings.xml', text: """<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>github</id>
            <username>\${env.GITHUB_CREDS_USR}</username>
            <password>\${env.GITHUB_CREDS_PSW}</password>
        </server>
    </servers>
</settings>
"""
            }
        }

        stage('Build with Maven') {
            steps {
                script {
                    echo 'Building package with Maven Wrapper...'
                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                        sh './mvnw clean package -s tmp-settings.xml -DskipTests'
                    } else {
                        bat 'mvnw.cmd clean package -s tmp-settings.xml -DskipTests'
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo 'Running unit tests...'
                    if (isUnix()) {
                        sh './mvnw test -s tmp-settings.xml'
                    } else {
                        bat 'mvnw.cmd test -s tmp-settings.xml'
                    }
                }
            }
            post {
                always {
                    // Archive unit test results in Jenkins
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            when {
                expression { fileExists('Dockerfile') }
            }
            steps {
                script {
                    echo "Building Docker image ${IMAGE_NAME}:${BUILD_NUMBER}..."
                    // Standard Jenkins docker-pipeline plugin usage
                    // Alternatively, you can run 'sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."'
                    if (isUnix()) {
                        sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
                    } else {
                        bat "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up temporary settings.xml...'
            // Clean up the settings file containing the GITHUB_CREDS to avoid credential leakage in workspace
            script {
                if (isUnix()) {
                    sh 'rm -f tmp-settings.xml'
                } else {
                    bat 'del tmp-settings.xml'
                }
            }
        }
        success {
            echo 'Pipeline built successfully!'
        }
        failure {
            echo 'Pipeline execution failed. Check console output for logs.'
        }
    }
}
