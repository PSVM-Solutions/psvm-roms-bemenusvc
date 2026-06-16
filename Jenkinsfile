pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    environment {
        GITHUB_CREDS = credentials('github-packages-token')
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
                    echo "Building Docker image ${IMAGE_NAME}:${env.BUILD_NUMBER}..."
                    docker.build("${IMAGE_NAME}:${env.BUILD_NUMBER}")
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up temporary settings.xml...'
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
