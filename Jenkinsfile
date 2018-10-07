pipeline{

    agent {
        node {
        label 'master'
    }
    
    parameters {
        	 choice(
            // choices are a string of newline separated values
            // https://issues.jenkins-ci.org/browse/JENKINS-41180
            choices: 'create\nupdate',
            description: '',
            name: 'REQUESTED_ACTION')
            
             choice(
            // choices are a string of newline separated values
            // https://issues.jenkins-ci.org/browse/JENKINS-41180
            choices: '100\n50',
            description: '',
            name: 'percentage')
    		}
}
   
stages{

	def userInput = input(id: ‘userInput’, message: ‘LetsPromote’, parameters: [
 			[$class: ‘TextParameterDefinition’, defaultValue: ‘uat’, description: ‘Environment’, name: ‘env’],
 			[$class: ‘TextParameterDefinition’, defaultValue: ‘uat1’, description: ‘Target’, name: ‘target’]
																					])

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
	//Go to global tool configuration and add the tool
 		
 	stage('maven')
 	{
 		steps
 		{
 			script
 			{
    			def mvnHome = tool name: 'maven'
    			env.PATH = "${mvnHome}:${env.PATH}"
			}
			sh 'mvn --version'
			echo '$env.PATH is = ' + env.PATH
		}
	}
	
	stage('choice')
	{
	
	when { 
		expression {params.REQUESTED_ACTION == 'create'}
		 }
	 steps{
	 		script{
	 		    echo 'create selected'
	 			}
		  }
	}

	stage('promotion')
	{
	 		steps{
			echo (“Env: “+userInput[‘env’])
			echo (“Target: “+userInput[‘target’])
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