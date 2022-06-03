pipeline {
    agent any
    //agent {
    //    label 'ec2_node'
    //}
    // agent {
    //     docker {
    //         image 'maven:3-jdk-8'
    //     }    
    // }
    stages{
        stage('parallel_test'){
            parallel{
                stage('a'){
                    steps{
                        echo "$JENKINS_HOME"
                    }
                }
                stage('Build Artifact - Maven') {
                  steps {
                      sh "mvn clean package -DskipTests=true"
                      archive 'target/*.jar'
                  }
                }
            }
        stage('Unit Tests - JUnit and JaCoCo') {
          steps {
            sh "mvn test"
          }
          post {
              always {
                junit 'target/surefire-reports/*.xml'
                jacoco execPattern: 'target/jacoco.exec'
               }
          }
        }
        stage('Vulnerability Scan ') {
          steps {
            sh "mvn dependency-check:check"
          }
          post {
            always {
              dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
            }
          }
        }  
        stage('take_input'){
            steps{
                    input('Do you want to proceed')
            }
        }
      }

    post{
        success{
            echo "from success block"
        }
        failure{
            echo "from failure block"
        }
        always {
            step([$class: 'Publisher', reportFilenamePattern: '**/target/surefire-reports/testng-results.xml'])
        }
    }    
  }