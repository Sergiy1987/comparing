    pipeline {
        agent any
         tools { nodejs "NodeJS" }
        environment {
            jobName = "$JOB_NAME"
            buildNumber = "${env.BUILD_NUMBER}"
            dateTime = "${env.BUILD_TIMESTAMP}"
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
            stage('Install dependencies') {
                steps {
                    //sh "npm install"
                    sh 'npm --version'
                    sh "npm install @percy/cli --save-dev"
                    sh "npm audit fix"
                }
            }
            stage('Chrome') {
                environment {
                    PERCY_TOKEN = "bd8b497f4312c2ce03a53eb32e8e9199f50387483593239b2c0368f6fa155c3d"
                }
                steps {
                        browserstack(credentialsId: 'browserstack') {
                        sh "export PERCY_TOKEN=${PERCY_TOKEN}"
                        echo "PERCY_TOKEN = ${env.PERCY_TOKEN}"
                        sh 'export PERCY_TOKEN=${PERCY_TOKEN} & mvn clean test -P chrome -Dtest=ChromeOrderTrackingPageTestsTestNg'//-P chrome
                        junit testDataPublishers: [[$class: 'AutomateTestDataPublisher']], testResults: 'target/surefire-reports/TEST-*.xml'
                        browserStackReportPublisher 'automate'
                    }
                }
            }
            stage('Firefox') {
                environment {
                    PERCY_TOKEN = "8403c430db27447139b3271134176f1cc6db8a31223143d1b70af4aa2ddef133"
                }
                    steps {
                        browserstack(credentialsId: 'browserstack') {
                        sh "export PERCY_TOKEN=${PERCY_TOKEN}"
                        echo "PERCY_TOKEN = ${env.PERCY_TOKEN}"
                        //sh 'export PERCY_TOKEN=${PERCY_TOKEN} & npx percy exec -- mvn test -Dtest=FirefoxOrderTrackingPageTests'
                        junit testDataPublishers: [[$class: 'AutomateTestDataPublisher']], testResults: 'target/surefire-reports/TEST-*.xml'
                        browserStackReportPublisher 'automate'
                    }
                }
            }
            stage('Safari') {
                environment {
                    PERCY_TOKEN = "c15768cbdd224d58651370e3304387ad2005411ce4322ea8e764c193fb037b37"
                }
                    steps {
                        browserstack(credentialsId: 'browserstack') {
                        sh "export PERCY_TOKEN=${PERCY_TOKEN}"
                        echo "PERCY_TOKEN = ${env.PERCY_TOKEN}"
                        //sh 'export PERCY_TOKEN=${PERCY_TOKEN} & npx percy exec -- mvn test -Dtest=SafariOrderTrackingPageTests'
                        junit testDataPublishers: [[$class: 'AutomateTestDataPublisher']], testResults: 'target/surefire-reports/TEST-*.xml'
                        browserStackReportPublisher 'automate'
                    }
                }
            }
            stage('Allure report') {
                steps {
                    script {
                        allure([includeProperties: false,jdk: '',
                        properties: [[key: 'allure.issues.tracker.pattern', value: 'https://route4me.atlassian.net/browse/%s']],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]])
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