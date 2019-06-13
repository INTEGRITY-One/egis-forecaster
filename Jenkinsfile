pipeline {
  agent any
  tools { 
    maven 'Maven 3.6.1' 
    jdk 'jdk8' 
  }
  stages {
    stage('Initialization') {
      steps {
        sh '''
          echo "PATH = ${PATH}"
          echo "M2_HOME = ${M2_HOME}"
        '''
      }
    }
    stage('Create Artifact BuildConfig') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector("bc", "forecaster-artifact-build").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.create(
               '{"apiVersion": "build.openshift.io/v1","kind": "BuildConfig","metadata":{"name": "forecaster-artifact-build","namespace": "egis","annotations":{"description": "Defines how to build the application artifact"},"labels":{"app": "forecaster"}},"spec":{"runPolicy": "Serial","source":{"git":{"uri": "https://github.com/INTEGRITY-One/egis-forecaster.git"},"type": "Git"},"strategy":{"type": "Source","sourceStrategy":{"from":{"kind": "ImageStream","name": "openjdk-11-rhel8","namespace": "openshift"}}},"output":{"to":{"kind": "ImageStreamTag","name": "forecaster-artifact:latest"}},"postCommit":{"script": ""},"successfulBuildsHistoryLimit": 3,"failedBuildsHistoryLimit": 3}}'
            )
          }
        }
      }
    }
    stage('Create Image BuildConfig') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector("bc", "forecaster-image-build").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.create(
               '{"apiVersion": "build.openshift.io/v1","kind": "BuildConfig","metadata":{"name": "forecaster-image-build","namespace": "egis","annotations":{"description": "Defines how to build the application image"},"labels":{"app": "forecaster"}},"spec":{"runPolicy": "Serial","source":{"dockerfile": "FROM graalvm:latest\\nVOLUME /tmp\\nARG JAR_FILE\\nWORKDIR /usr/local/\\nCOPY forecaster-1.0-SNAPSHOT.jar ./forecaster.jar\\nCOPY forecaster-1.0-SNAPSHOT-runner.jar ./forecaster-runner.jar\\nCOPY lib ./lib\\nENTRYPOINT [\\"java\\",\\"-Djava.security.egd=file:/dev/./urandom\\",\\"-jar\\",\\"/usr/local/forecaster-runner.jar\\"]","images":[{"from":{"kind": "ImageStreamTag","namespace": "egis","name": "forecaster-artifact:latest"},"paths":[{"sourcePath": "/tmp/src/target/forecaster-1.0-SNAPSHOT.jar","destinationDir": "."},{"sourcePath": "/tmp/src/target/forecaster-1.0-SNAPSHOT-runner.jar","destinationDir": "."},{"sourcePath": "/tmp/src/target/lib","destinationDir": "."}]}]},"strategy":{"dockerStrategy":{"from":{"kind": "ImageStreamTag","name": "graalvm:latest","namespace": "openshift"}}},"output":{"to":{"kind": "ImageStreamTag","name": "forecaster:latest"}},"postCommit":{"script": ""},"successfulBuildsHistoryLimit": 3,"failedBuildsHistoryLimit": 3}}'
            )
          }
        }
      }
    }
    stage ('Execute Build to Native Binary') {
      steps {
          sh 'mvn clean package -Pnative' 
      }
      post {
          success {
              echo "Built native executable!"
          }
      }
    }
    stage('Execute Artifact Build') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "forecaster-artifact-build").startBuild("--wait")
          }
        }
      }
    }
    stage('Execute Image Build') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "forecaster-image-build").startBuild("--wait")
          }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("forecaster:latest", "forecaster:dev")
          }
        }
      }
    }
    stage('Create DEV') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'forecaster-dev').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("forecaster:latest", "--name=forecaster-dev").narrow('svc').expose()
          }
        }
      }
    }
    stage('Promote to STAGE') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("forecaster:dev", "forecaster:stage")
          }
        }
      }
    }
    stage('Create STAGE') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'forecaster-stage').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("forecaster:stage", "--name=forecaster-stage").narrow('svc').expose()
          }
        }
      }
    }
  }
}