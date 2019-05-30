pipeline {
  agent {
  	label 'maven'
  }
  stages {
    stage('Create BuildConfig') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector("bc", "forecaster-buildconfig").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.create(
               '{"apiVersion": "build.openshift.io/v1","kind": "BuildConfig","metadata": {"annotations": {"description": "Defines how to build the application"},"labels": {"app": "forecaster"},"name": "forecaster-buildconfig","namespace": "egis"},"spec": {"failedBuildsHistoryLimit": "5","output": {"to": {"kind": "ImageStreamTag","name": "forecaster:latest"}},"postCommit": {"script": ""}, "runPolicy": "Serial","source": {"git": {"uri": "https://github.com/INTEGRITY-One/egis-forecaster.git"},"type": "Git"},"strategy": {"type": "Source","sourceStrategy": {"from": {"kind": "ImageStream","namespace": "openshift","name": "openjdk-11-rhel8"}}},"successfulBuildsHistoryLimit": "5"}}'
            )
          }
        }
      }
    }
    stage('Execute Build') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "forecaster-buildconfig").startBuild("--wait")
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