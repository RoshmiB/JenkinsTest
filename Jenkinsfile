def curlRun (url, out) {
    echo "Running curl on ${url}"

    script {
        if (out.equals('')) {
            out = 'http_code'
        }
        echo "Getting ${out}"
            def result = sh (
                returnStdout: true,
                script: "curl --output /dev/null --silent --connect-timeout 5 --max-time 5 --retry 5 --retry-delay 5 --retry-max-time 30 --write-out \"%{${out}}\" ${url}"
        )
        echo "Result (${out}): ${result}"
    }
}

def curlTest (namespace, out) {
    echo "Running tests in ${namespace}"

    script {
        if (out.equals('')) {
            out = 'http_code'
        }

        // Get deployment's service IP
        def svc_ip = sh (
                returnStdout: true,
                script: "kubectl get svc -n ${namespace} | grep ${ID} | awk '{print \$3}'"
        )

        if (svc_ip.equals('')) {
            echo "ERROR: Getting service IP failed"
            sh 'exit 1'
        }

        echo "svc_ip is ${svc_ip}"
        url = 'http://' + svc_ip

        curlRun (url, out)
    }
}

def createNamespace (namespace) {
    echo "Creating namespace ${namespace} if needed"

    sh "[ ! -z \"\$(kubectl get ns ${namespace} -o name 2>/dev/null)\" ] || kubectl create ns ${namespace}"
}

def helmDelete (namespace, release) {
    echo "Deleting ${release} in ${namespace} if deployed"

    script {
        release = "${release}-${namespace}"
        sh "[ -z \"\$(helm ls --short ${release} 2>/dev/null)\" ] || helm delete --purge ${release}"
    }
}

def helmInstall (namespace, release) {
    echo "Installing ${release} in ${namespace}"

    script {
        release = "${release}-${namespace}"
        sh "helm repo add ${release} ${HELM_REPO}; helm repo update"
        sh """
            helm upgrade --install --namespace ${namespace} ${release} \
//                --set imagePullSecrets=${IMG_PULL_SECRET} \
                --set image.repository=${DOCKER_REPO}/${IMAGE_NAME},image.tag=${DOCKER_TAG} ${WORKSPACE}/weatherapp-ui
        """
        sh "sleep 5"
    }
}



pipeline{
    agent any
    options{
      timeout(time:60, unit: 'MINUTES')
    }
    environment {
        IMAGE_NAME = 'weatherapp-ui'
        TEST_LOCAL_PORT = '3000'
    }
    parameters{
      string(name: 'GIT_BRANCH', defaultValue: 'test7' , description: 'Git branch to build')
      string(name: 'DOCKER_REPO',       defaultValue: "roshmi",                   description: 'Docker hub repo')
      string(name: 'DOCKER_TAG',       defaultValue: 'dev',                                     description: 'Docker tag')
      string(name: 'HELM_REPO',        defaultValue: "s3://helm-weather/charts", description: 'Your helm repository')
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
                
                  sh """
                      cd ${WORKSPACE}/UI
                      docker build -t ${DOCKER_REPO}/${IMAGE_NAME}:${DOCKER_TAG} . || errorExit 'Building ${DOCKER_REPO}:${DOCKER_TAG} failed'
                      """ 
                
                echo "Running tests"

                // Kill container in case there is a leftover
                sh "[ -z \"\$(docker ps -a | grep ${ID} 2>/dev/null)\" ] || docker rm -f ${ID}"

                echo "Starting ${IMAGE_NAME} container"
                sh "docker run --detach --name ${ID} --rm --publish ${TEST_LOCAL_PORT}:3000 ${DOCKER_REPO}/${IMAGE_NAME}:${DOCKER_TAG}"

                script {
                host_ip = sh(returnStdout: true, script: "/sbin/ifconfig en0 | grep 'inet ' | cut -d ' ' -f2 | awk '{ print \$1 }'")
                host_address = "${host_ip}".trim()+":${TEST_LOCAL_PORT}"
                echo "${host_address}"
                }
            }
        } 

        // Run the 3 tests on the currently running weatherapp-ui Docker container
        stage('Local tests') {
            parallel {
                stage('Curl http_code') {
                    steps {
                        curlRun ("http://${host_address}", 'http_code')
                    }
                }
                stage('Curl time_total') {
                    steps {
                        curlRun ("http://${host_address}", 'time_total')
                    }
                }
                stage('Curl size_download') {
                    steps {
                        curlRun ("http://${host_address}", 'size_download')
                    }
                }
            }
        }

        ////////// Step 3 //////////
        stage('Publish Docker and Helm') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'DockerHubPwd', usernameVariable: 'USERNAME' , passwordVariable: 'PASSWORD')]) {
                  sh "docker login -u ${USERNAME} -p ${PASSWORD}"

                  echo "Stop and remove container"
                  sh "docker stop ${ID}"

                  echo "Pushing ${DOCKER_REPO}/${IMAGE_NAME}:${DOCKER_TAG} image to registry"
                  sh "docker push ${DOCKER_REPO}/${IMAGE_NAME}:${DOCKER_TAG}"
                }
                
                echo "Packing helm chart"
                sh """
                        mkdir -p ${WORKSPACE}/helm
                        helm package -d ${WORKSPACE}/helm ${WORKSPACE}/weatherapp-ui || errorExit "Packing helm chart ${WORKSPACE}/weatherapp-ui failed"
                        chart_name="\$( ls -1 "${WORKSPACE}"/helm/*.tgz 2> /dev/null)"
                        echo "\${chart_name}"
                        helm s3 push --force "\${chart_name}" my-charts
                        echo  "Pushing Helm chart"
                        rm -rf ${WORKSPACE}/helm
                    """       
            }
        }

        ////////// Step 4 //////////
        stage('Deploy to dev') {
            steps {
                script {
                    namespace = 'development'

                    echo "Deploying application ${ID} to ${namespace} namespace"
                    createNamespace (namespace)

                    // Remove release if exists
                    helmDelete (namespace, "${ID}")

                    // Deploy with helm
                    echo "Deploying"
                    helmInstall(namespace, "${ID}")
                }
            }
        }

        // Run the 3 tests on the deployed Kubernetes pod and service
        stage('Dev tests') {
            parallel {
                stage('Curl http_code') {
                    steps {
                        curlTest (namespace, 'http_code')
                    }
                }
                stage('Curl time_total') {
                    steps {
                        curlTest (namespace, 'time_total')
                    }
                }
                stage('Curl size_download') {
                    steps {
                        curlTest (namespace, 'size_download')
                    }
                }
            }
        }

        // stage('Cleanup dev') {
        //     steps {
        //         script {
        //             // Remove release if exists
        //             helmDelete (namespace, "${ID}")
        //         }
        //     }
        // }


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