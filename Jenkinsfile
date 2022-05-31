pipeline{
	
  agent 
    {
      kubernetes{
        label 'helm-pod'
        containerTemplate {
          name 'helm-pod'
          image 'roshmi/helm-s3'
          ttyEnabled true
          command 'cat'
        }  
      }
    }

	stages{
    stage ('deploy_using_helm'){
      steps{
        container('helm-pod'){
          sh '''
            HELM_BUCKET=helm-weather
            PACKAGE=my-charts
            export AWS_REGION=ap-south-1

            helm repo add my-charts s3://helm-weather/charts
            DEPLOYED=$(helm list |grep -E "^${PACKAGE}" |grep DEPLOYED |wc -l)
            if [ $DEPLOYED == 0 ] ; then
              helm install --name ${PACKAGE} my-charts/${PACKAGE}
            else
              helm upgrade ${PACKAGE} my-charts/${PACKAGE}
            fi
            echo "deployed!"
          '''
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
