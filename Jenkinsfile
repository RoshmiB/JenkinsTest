pipeline {
    //agent any
    //agent {
    //    label 'ec2_node'
    //}
    agent {
        docker {
            image 'maven:3-jdk-8'
        }    
    }
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
                        sh "mvn install"
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