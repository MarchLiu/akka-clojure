pipeline {
    agent {
        docker {
            image 'marsliu/clojure-devel-studio:jdk-11'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'lein do clean, compile'
            }
        }
        stage('Test') {
            steps {
                sh 'lein test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}