pipeline{
	
  agent any
  parameters   {
    string(name: 'TARGET_ENV', devaultValue: 'PROD', description: 'Environment')
  }

	stages{
    stage ('BUILD'){
      steps{
        echo 'Compiling......'
        sleep 5
      }
    }
    stage ('Deploy') {
      steps{
        script{
          build job: 'ReleaseJob'
          parameters[
            [$class: 'StringParameterValue', name: 'FROM_BUILD', value: "${BUILD_NUMBER}"]
          ]
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


//aws s3api create-bucket --bucket helm-weather --region ap-south-1 --create-bucket-configuration LocationConstraint=ap-south-1
//helm plugin install https://github.com/hypnoglow/helm-s3.git
//helm s3 init s3://helm-weather/charts
//helm repo add my-charts s3://helm-weather/charts
//helm package weatherapp-weather
//helm s3 push ./weatherapp-weather-0.1.0.tgz my-charts
//mkdir /var/lib/jenkins/.kube
//copy config file under .kube directory with jenkins ownership
//helm search repo my-charts
//NAME                                CHART VERSION   APP VERSION     DESCRIPTION
//my-charts/weatherapp-weather    0.1.0           0.0.1           The weather microservice for the weather app
