pipeline{
	
  agent any

	stages{
    stage ('deploy_using_helm'){
      steps{
        // container('helm-pod'){
          sh '''
            HELM_BUCKET=helm-weather
            PACKAGE=weatherapp-weather
            export AWS_REGION=ap-south-1

            helm repo add my-charts s3://helm-weather/charts
            helm search repo my-charts
            DEPLOYED=$(helm list |grep -E "^${PACKAGE}" |grep DEPLOYED |wc -l)
            echo $DEPLOYED
            if [ $DEPLOYED == 0 ] ; then
              helm install ${PACKAGE} my-charts/${PACKAGE} --set apikey=ecbc396f46mshb65cbb1f82cf334p1fcc87jsna5e962a3c542
            else
              helm upgrade ${PACKAGE} my-charts/${PACKAGE} --set apikey=ecbc396f46mshb65cbb1f82cf334p1fcc87jsna5e962a3c542
            fi
            echo "deployed!"
          '''
        // }
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
//helm install weatherapp-weather  my-charts/weatherapp-weather --set apikey=ecbc396f46mshb65cbb1f82cf334p1fcc87jsna5e962a3c542
