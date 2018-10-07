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
         
		
	// How to initialize software and set path
 		
 	stage('ansible')
 	{
 		steps
 		{
 			script
 			{
    			def ansibleHome = tool name: 'ansible'
    			env.PATH = "${ansibleHome}:${env.PATH}"
			}
			sh 'ansible --version'
		}
	}
		
		
      }
    
 post{
        always{
        echo "from always block"
        }
        success{
        echo "from success block"
        }
        failure{
        echo "Send eamil as its failed "
        }
    }
}