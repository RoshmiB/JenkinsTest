pipeline{

    agent any
   
stages{
    stage('build')
        {
      steps{
          script{
            echo $Helloo
            sh "mvn clean install"
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