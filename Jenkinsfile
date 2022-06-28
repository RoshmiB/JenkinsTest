pipeline{
    agent any
    options{
      timeout(time:60, unit: 'MINUTES')
    }
    environment {
        IMAGE_NAME = 'weatherapp-ui'
        TEST_LOCAL_PORT = '80'
    }
    parameters{
      string(name: 'GIT_BRANCH', defaultValue: 'test7' , description: 'Git branch to build')
      string(name: 'DOCKER_REPO',       defaultValue: "roshmi",                   description: 'Docker hub repo')
      string(name: 'DOCKER_TAG',       defaultValue: 'dev',                                     description: 'Docker tag')
      string(name: 'HELM_REPO',        defaultValue: "https://helm-weather.s3.ap-south-1.amazonaws.com/charts/", description: 'Your helm repository')
    }
    stages{
        stage("git clone and setup"){
            steps{
                echo "Check out  code"
                git branch: "test7",
                        credentialsId: 'gitcred',
                        url: 'https://github.com/RoshmiB/JenkinsTest.git'
                // Validate kubectl
                sh "kubectl cluster-info"
                // Init helm client
                sh "helm version"
                echo "DOCKER_REPO is ${DOCKER_REPO}"
                echo "HELM_REPO  is ${HELM_REPO}"
                // Define a unique name for the tests container and helm release
                script {
                    branch = GIT_BRANCH.replaceAll('/', '-').replaceAll('\\*', '-')
                    ID = "${IMAGE_NAME}-${DOCKER_TAG}-${branch}"
                    echo "Global ID set to ${ID}"
                }
            }
        }
        stage('Build and tests') {
            steps {
                echo "Building application and Docker image"
                withCredentials([string(credentialsId: 'DockerHubPwd', Username: 'DOCKER_REGISTRY_USER' , password: 'DOCKER_REGISTRY_PWD')]) {
                  sh "docker login -u ${DOCKER_REGISTRY_USER} -p ${DOCKER_REGISTRY_PWD}"
                  sh "cd ${WORKSPACE}/UI"
                  sh """docker build -t ${DOCKER_REPO}/${IMAGE_NAME}:${DOCKER_TAG} . || errorExit 'Building ${DOCKER_REPO}:${DOCKER_TAG} failed'""" 
                }
                echo "Running tests"

                // Kill container in case there is a leftover
                sh "[ -z \"\$(docker ps -a | grep ${ID} 2>/dev/null)\" ] || docker rm -f ${ID}"

                echo "Starting ${IMAGE_NAME} container"
                sh "docker run --detach --name ${ID} --rm --publish ${TEST_LOCAL_PORT}:80 ${DOCKER_REPO}/${IMAGE_NAME}:${DOCKER_TAG}"

                script {
                    host_ip = sh(returnStdout: true, script: '/sbin/ip route | awk \'/default/ { print $3 ":${TEST_LOCAL_PORT}" }\'')
                }
            }
        } 

        // Run the 3 tests on the currently running weatherapp-ui Docker container
        stage('Local tests') {
            parallel {
                stage('Curl http_code') {
                    steps {
                        curlRun ("http://${host_ip}", 'http_code')
                    }
                }
                stage('Curl total_time') {
                    steps {
                        curlRun ("http://${host_ip}", 'total_time')
                    }
                }
                stage('Curl size_download') {
                    steps {
                        curlRun ("http://${host_ip}", 'size_download')
                    }
                }
            }
        } 


    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ========"
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}