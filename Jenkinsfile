pipeline{

    agent {
//        node {
//        label 'master'
//			    }

	docker {
        image 'maven'
        args '-v $HOME/.m2:/root/m2'
        	}
    	}
    parameters {
        	 choice(
            // choices are a string of newline separated values
            // https://issues.jenkins-ci.org/browse/JENKINS-41180
			name: 'REQUESTED_ACTION',
            choices: 'create\nupdate', 
            description: '')
            
             choice(
            // choices are a string of newline separated values
            // https://issues.jenkins-ci.org/browse/JENKINS-41180
            name: 'percentage',
            choices: '100\n50',
            description: '')
    		}
    		
    options {
      		timeout(time: 30,unit: 'MINUTES')
      		disableConcurrentBuilds()
    		}
		
	environment { 
            DEBUG_FLAGS = '-g'
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
	
	stage('choice_dropdown')
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

	stage('promotion_userinput')
	{
	
			def userInput = input { id ‘userInput’
								  message ‘LetsPromote’ 
								  ok 'Promote!!!'
								  parameters: {
 			string(defaultValue: ‘uat’, description: ‘Environment’, name: ‘env’)
 			string(defaultValue: 2, description: ‘Target’, name: ‘target’)
				} }
	
	 		steps{
	 		
	 		def inputenv = userInput[‘env’]
	 		def inputtarget = userInput[‘target’]

			echo 'Env: '+userInput[‘env’]
			echo 'Target: '+userInput[‘target’]
			
			writeFile file: "/Users/roshmi.b/Desktop/inputData.txt", text: "Config=${inputenv}\r\nTest=${inputtarget}"
			archiveArtifacts '/Users/roshmi.b/Desktop/inputData.txt'
				}
	}
	
	stage('env_variable') {
            steps {
                sh 'printenv'
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