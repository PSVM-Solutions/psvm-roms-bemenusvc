pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    environment {
        GITHUB_CREDS = credentials('github-packages-token')
        DOCKER_HUB_USER = credentials('DOCKER_HUB_USER')
        DOCKER_HUB_REPO = credentials('DOCKER_HUB_REPO')        
        // Bind the MongoDB credentials configured in Jenkins credentials store
        MONGODB_ROOT_USERNAME = credentials('MONGODB_ROOT_USERNAME')
        MONGODB_ROOT_PASSWORD = credentials('MONGODB_ROOT_PASSWORD')
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

        stage('Docker Build & Push') {
            when {
                expression { fileExists('Dockerfile') }
            }
            steps {
                script {
                    try {
                        docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                            def tag = "menu-service-${env.BUILD_NUMBER}"
                            echo "Building Docker image ${DOCKER_HUB_USER}/${DOCKER_HUB_REPO}:${tag}..."
                            def customImage = docker.build("${DOCKER_HUB_USER}/${DOCKER_HUB_REPO}:${tag}")
                            
                            echo "Pushing Docker image..."
                            customImage.push()
                            customImage.push("menu-service-latest")
                        }
                    } catch (Exception e) {
                        echo "WARNING: Docker build/push failed: ${e.message}"
                        echo "Please ensure the Jenkins agent has the Docker CLI installed, access to the Docker daemon, and credentials configured."
                        currentBuild.result = 'UNSTABLE'
                    }
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
