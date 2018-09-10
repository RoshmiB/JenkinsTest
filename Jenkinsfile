pipeline{

    agent {
        node {
        label 'master'
    }
}
   
stages{
    stage('build')
        {
      steps{
          script{
            echo "Helloo"
            sh "mvn clean install -Dmaven.test.skip"
                }
            }
         }
       }
    
 post{
        always{
        echo "from always"
        }
        success{
        echo "from success"
        }
        failure{
        echo "Send eamil as its failed "
        }
    }
}