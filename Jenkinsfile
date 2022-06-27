pipeline{
    agent any
    options{
      timeout(time:60, unit: 'MINUTES')
    }
    parameters{
      string(name: 'GIT_BRANCH', defaultValue: 'test7' , description: 'Git branch to build')
      string(name: 'DOCKER_REG',       defaultValue: "https://hub.docker.com",                   description: 'Docker registry')
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
                sh "helm init"
                echo "DOCKER_REG is ${DOCKER_REG}"
                echo "HELM_REPO  is ${HELM_REPO}"
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