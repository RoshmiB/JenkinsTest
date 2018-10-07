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
         
    stage('test')
    	{
    	steps{
    		script{
    	    def ans = input ('Do u want the test to run ? ans (Y/N) ')
    	    
    	    if ( ans == "Y" ){
    	                    sh "mvn test"
    	                	}
    	    else 
    	    	junit  'build/reports/test/result.xml'    
    			}
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