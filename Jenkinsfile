podTemplate(label: 'testing', 
  containers: [
    containerTemplate(name: 'kubectl', image: 'jorgeacetozi/kubectl:1.7.0', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat'),
  ],
  volumes: [
    hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
    secretVolume(mountPath: '/etc/maven/', secretName: 'maven-settings-secret')
  ],
  envVars: [
    secretEnvVar(key: 'DOCKERHUB_USERNAME', secretName: 'dockerhub-username-secret', secretKey: 'USERNAME'),
    secretEnvVar(key: 'DOCKERHUB_PASSWORD', secretName: 'dockerhub-password-secret', secretKey: 'PASSWORD'),
  ])
{
  node ('testing') {

  try{
      notifySlack('STARTED')

    def image_name = "auth-service"

    checkout scm

    dir('app') {
      stage('Checkout the Auth-Service application') {
        git url: 'https://github.com/bhankol-io/bhankol-auth.git', branch: "${GIT_BRANCH}"
      }

      stage('Run Unit/Integration Tests, generate the jar artifact and push it to Artifactory') {
        container('maven') {
          sh 'mvn -B -s /etc/maven/settings.xml clean deploy'
        }
      }

      stage('Build and push a new Docker image with the tag based on the Git branch') {
        container('docker') {
          sh """
            docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD}
            docker build -t ${DOCKERHUB_USERNAME}/${image_name}:${GIT_BRANCH} .
            docker push ${DOCKERHUB_USERNAME}/${image_name}:${GIT_BRANCH}
          """
        }
      }
    }

    stage('Deploy to Testing environment') {
          container('kubectl') {
            sh """
              kubectl config set-context testing --namespace=testing --cluster=k8s.itbitstechnologies.com --user=k8s.itbitstechnologies.com
              kubectl config use-context testing

              sed -i "s/AUTHSERVICE_CONTAINER_IMAGE/${DOCKERHUB_USERNAME}\\/${image_name}:${GIT_BRANCH}/" authservice/testing/authservice-testing-deployment.yaml


              kubectl apply -f notepad/testing/ -l app=notepad
              kubectl rollout status deployment notepad-deployment-testing

              kubectl get service notepad-service-testing
              kubectl get endpoints notepad-service-testing
            """
          }

        }





  } catch (e) {
          currentBuild.result = 'FAILURE'
          throw e
      } finally {
          def finalResult = currentBuild.result ?: 'SUCCESS'
          notifySlack(currentBuild.result)
      }
}
}
def notifySlack(String buildStatus = 'STARTED') {


   buildStatus = buildStatus ?: 'SUCCESS'

    def color

    if (buildStatus == 'STARTED') {
        color = '#D4DADF'
    } else if (buildStatus == 'SUCCESS') {
        color = '#BDFFC3'
    } else if (buildStatus == 'UNSTABLE') {
        color = '#FFFE89'
    } else {
        color = '#FF9FA1'
    }

    def msg = "${buildStatus}: '${env.JOB_NAME}' #${env.BUILD_NUMBER}:\n${env.BUILD_URL}"

    slackSend(color: color, message: msg)
}
