pipeline {
    agent any

    stages{
    // stage('parallel_test'){
    //     steps{
    //         parallel{
    //             a:{
    //                 echo "$JENKINS_HOME"
    //             }
    //             b: {
    //                 sh "git --version"
    //             }
    //         }
    //     }
    // }
        stage('take_input'){
            steps{
                echo "input"
            }
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