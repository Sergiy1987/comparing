    pipeline {
        agent any
         tools { nodejs "NodeJS" }
        environment {
            jobName = "$JOB_NAME"
            buildNumber = "${env.BUILD_NUMBER}"
            dateTime = "${env.BUILD_TIMESTAMP}"
            PERCY_TOKEN = "daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223"
        }
        stages {
            stage('Clean workspace') {
                steps {
                    //notifySlack()
                    echo "workspace directory is ${env.WORKSPACE}"
                    echo "build URL is ${env.BUILD_URL}"
                    echo "workspace build number is ${env.BUILD_NUMBER}"
                    /* clean up our workspace */
                        deleteDir()
                    /* clean up tmp directory */
                    dir("${workspace}@tmp") {
                        deleteDir()
                    }
                    /* clean up script directory */
                    dir("${workspace}@script") {
                        deleteDir()
                    }
                }
            }
            stage('Clone repository') {
                steps {
                    git branch: 'master',
                    /*credentialsId: 'prod_jenkins',*/
                    url: 'https://github.com/Sergiy1987/comparing.git'
                }
            }
            stage('Visability tests') {
                steps {
                    sh "export PERCY_TOKEN=${PERCY_TOKEN}"
                    echo "PERCY_TOKEN = ${env.PERCY_TOKEN}"
                    sh 'npm --version'
                    //sh "npm install"
                    sh "npm install @percy/cli --save-dev"
                    sh "npm audit fix"
                    wrap([$class: 'Xvfb', additionalOptions: '', assignedLabels: '', autoDisplayName: true, debug: true, displayNameOffset: 0, installationName: 'xvfb', parallelBuild: true, screen: '1600x1200x24', timeout: 10]) {
                    sh 'npx percy exec -- mvn test'+
                    ' -Duser.timezone=Europe/Kiev'
                    }
                }
            }
            stage('Allure report') {
                steps {
                    script {
                        allure([includeProperties: false,jdk: '',
                        properties: [[key: 'allure.issues.tracker.pattern', value: 'https://route4me.atlassian.net/browse/%s']],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'allure-results']]])
                    }
                }
            }
        }

        post {
            unstable {
                script {
                    //mail to: "sergiys@route4me.com, arturm@route4me.com, max@route4me.com, ezz@route4me.com",
                    mail to: "sergiys@route4me.com",
                    subject: "Production ${currentBuild.currentResult} Pipeline: ${currentBuild.fullDisplayName}",
                    body: "Production Build ${currentBuild.fullDisplayName} is finished with status ${currentBuild.currentResult}. Test results are attached ${env.BUILD_URL}allure"
                }
            }
            failure {
                script {
                    //mail to: "sergiys@route4me.com, arturm@route4me.com, max@route4me.com, ezz@route4me.com",
                    mail to: "sergiys@route4me.com",
                    subject: "Production ${currentBuild.currentResult} Pipeline: ${currentBuild.fullDisplayName}",
                    body: "Production Build ${currentBuild.fullDisplayName} is finished with status ${currentBuild.currentResult}. Test results are attached ${env.BUILD_URL}allure"
                }
            }
            aborted {
                echo "It was aborted!"
            }
        }
    }

    def notifySlack(String buildStatus = 'STARTED') {
    // Build status of null means success.
    buildStatus = buildStatus ?: 'SUCCESS'
    def color
    def iconStarted = ":man_dancing:"
    def iconSuccess = ":tada:"
    def iconFailure = ":hot_face:"
    def iconUnstable = ":mask:"
    def msgStarted = "*${buildStatus}* ${iconStarted}${iconStarted}${iconStarted} `${env.JOB_NAME}` #${env.BUILD_NUMBER}\n${env.BUILD_URL}"
    def msgFinished = "*${buildStatus}*: `${env.JOB_NAME}` #${env.BUILD_NUMBER}\n *Allure Report:* <${env.BUILD_URL}allure|Report link>"

    sh 'echo "${buildStatus}"'
    if (buildStatus == 'STARTED') {}
    else if (buildStatus == 'SUCCESS') {}
    else if (buildStatus == 'ABORTED') {}
    else if (buildStatus == 'UNSTABLE') {
        color = '#FFA500'
        slackSend(color: color, message: iconFailure+iconFailure+iconFailure+ msgFinished + iconUnstable+iconUnstable+iconUnstable)
    }
    else if (buildStatus == 'FAILURE') {
        color = '#FF0000'
        slackSend(color: color, message: iconFailure+iconFailure+iconFailure+ msgFinished + iconUnstable+iconUnstable+iconUnstable)
    }
}