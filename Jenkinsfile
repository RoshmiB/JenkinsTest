pipeline{
	
 agent any

/*  agent {
//        node {
//        label 'master'
//			    }

	docker {
        image 'maven'
        label 'master'
        args '-v $HOME/.m2:/root/m2'
        	}
    	}*/
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
            VARIABLE = 'roshmi'
            }	
   
stages{
		
      stage('build')
        {
       steps{
          script{
            echo "Helloo"
            echo 'Pulling...' + env.BRANCH_NAME
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

	stage('Example') {
            input {
                message "Should we continue?"
                ok "Yes, we should."
                //submitter "alice,bob"
                parameters {
                    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
                    string(name: 'PASSWORD', defaultValue: 'Mr Jenkins', description: 'pass')
                }
            }
            steps {
                echo "Hello, ${PERSON}, nice to meet you."

			//writeFile file: "/Users/roshmi.b/Desktop/inputData.txt", text: "Config=${PERSON}\r\nTest=${PASSOWRD}"
			//archiveArtifacts '/Users/roshmi.b/Desktop/inputData.txt'
				}
			}
	
       stage('env_variable') {
            steps {
            	script{
                sh 'printenv'
                echo env.VARIABLE
                }
              }
            }
// to deploy:- Jenikins-->Manage Jenkins-->Manage files-->Maven settings.xml       

      stage('build2') {
          steps {
           script{
             echo env.MAVEN_SETTINGS
	     def pom = readMavenPom file: 'pom.xml'  //returned object is a model
             def ver = pom['version']       //${pom.version} --> extracting the value from the model object
             def mvn_dir = "/usr/bin/mvn"
	     def branch = env.BRANCH_NAME
		  if (branch.contains('master') && !(version.contains('SNAPSHOT'))){
	    		 configFileProvider([configFile(fileId: 'myconfig', variable: 'MyGlobalSettings')]) {
				 sh "mvn install -Dmaven.test.skip=true -s $MyGlobalSettings" }
			  
                       }
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
