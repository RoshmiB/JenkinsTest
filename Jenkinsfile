pipeline {
    agent any

    stages{
    stage('parallel_test'){
        parallel{
                stage('a'){
                    steps{
                        echo "$JENKINS_HOME"
                    }
                }
                stage('b') {
                    steps{
                        sh "git --version"
                    }
                }
            }
    }
        stage('take_input'){
            steps{
                    input('Do you want to proceed')
            }
        }
    }

    post{
        success{
            echo "from success block"
        }
        failure{
            echo "from failure block"
        }
    }    
}