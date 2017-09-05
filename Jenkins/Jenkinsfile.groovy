def module

node {
    git url: 'git@bitbucket.org:redbrush2/serenity-with-jbehave-and-spring.git', branch: 'master', credentialsId: 'BitBucket_Redbrush'

    // Define parameters which will use in the JenkinsFile
    // Set Jenkins Job as Parameterized Job and it will set up default parameters
    properties([
            // Test Purpose
            pipelineTriggers([
                    upstream(
                            threshold: hudson.model.Result.SUCCESS,
                            upstreamProjects: 'Test job'
                    )
            ]),
            //pipelineTriggers([[$class: 'BitBucketTrigger']]),
            [
                    $class              : 'ParametersDefinitionProperty',
                    parameterDefinitions: [
                            [
                                    $class      : 'StringParameterDefinition',
                                    defaultValue: 'local',
                                    description : '',
                                    name        : 'activeProfile'
                            ]
                    ]
            ]
    ])

    // set up maven
    def mvnHome
    def reportName
    def workspace = pwd()

    echo "Workspace = ${workspace}"

    if (isUnix()) {
        module = load "${workspace}/Jenkinsfile_module.groovy"
    } else {
        module = load "..\\workspace\\Jenkinsfile_module.groovy"
    }

    echo "module = ${module}"

    stage('Preparation') {
        // For display purpose
        echo "Current workspace : ${workspace}"

        // Get the Maven tool.
        // This 'M3' Maven tool must be configured in the global configuration in Jenkins
        mvnHome = tool 'apache-maven-3.5.0'

        reportName = "Serenity report ${BUILD_NUMBER}"

        def causes = currentBuild.rawBuild.getCauses()

        for (cause in causes) {
            module.printCausesRecursively(cause)
        }
    }

    stage('Checkout') {
        // Get code from a Git repository
        checkout scm
    }

    // This is the current syntax for invoking a build wrapper, naming the class.
    // Execute tests and product reports
    // In order to use AnsiColor, you must install AnsiColor Plugin at https://wiki.jenkins-ci.org/display/JENKINS/AnsiColor+Plugin
    stage('Test') {
        ansiColor('xterm') {
            sh "'${mvnHome}/bin/mvn' -P ${activeProfile} clean verify"
        }
    }

    stage('PublishHTML') {
        publishHTML(target: [
                reportName : reportName,
                reportDir : 'target/site/serenity',
                reportFiles: 'index.html',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMission: false
        ])
    }

    // Relax CSS stripe issue after report generation.
    // Run this command on the Jenkins script console
    // System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")
}