pipeline {
	agent any
	tools {
		jdk 'jdk8'
	}
	environment {
		PIPELINE_VERSION = VersionNumber(
			versionNumberString: '${BUILD_DATE_FORMATTED, \"yyMMdd_HHmmss\"}',
			versionPrefix: '1.0.0.M1-'
		)
		CF_TEST_API_URL = 'api.local.pcfdev.io'
		CF_STAGE_API_URL = 'api.local.pcfdev.io'
		CF_PROD_API_URL = 'api.local.pcfdev.io'
		CF_TEST_ORG = 'pcfdev-org'
		CF_TEST_SPACE = 'pcfdev-test'
		CF_STAGE_ORG = 'pcfdev-org'
		CF_STAGE_SPACE = 'pcfdev-stage'
		CF_PROD_ORG = 'pcfdev-org'
		CF_PROD_SPACE = 'pcfdev-prod'
		CF_HOSTNAME_UUID = ''

		M2_SETTINGS_REPO_ID = 'artifactory-local'
		REPO_WITH_JARS = 'http://artifactory:8081/artifactory/libs-release-local'

		GIT_CREDENTIAL_ID = 'git'
		GIT = credentials('git')

		CF_TEST = credentials('cf-test')
		CF_STAGE = credentials('cf-stage')
		CF_PROD = credentials('cf-prod')

		TOOLS_REPOSITORY = 'https://github.com/wybczu/spring-cloud-pipelines'
		TOOLS_BRANCH = '*/master'
	}
	parameters {
		booleanParam(
			name: 'REDOWNLOAD_INFRA',
			defaultValue: false,
			description: "If Eureka & StubRunner & CF binaries should be redownloaded if already present"
		)
		booleanParam(
			name: 'REDEPLOY_INFRA',
			defaultValue: true,
			description: "If Eureka & StubRunner binaries should be redeployed if already present"
		)
		string(
			name: 'EUREKA_GROUP_ID',
			defaultValue: 'com.example.eureka',
			description: "Group Id for Eureka used by tests"
		)
		string(
			name: 'EUREKA_ARTIFACT_ID',
			defaultValue: 'github-eureka',
			description: "Artifact Id for Eureka used by tests"
		)
		string(
			name: 'EUREKA_VERSION',
			defaultValue: '0.0.1.M1',
			description: "Artifact Version for Eureka used by tests"
		)
		string(
			name: 'STUBRUNNER_GROUP_ID',
			defaultValue: 'com.example.github',
			description: "Group Id for Stub Runner used by tests"
		)
		string(
			name: 'STUBRUNNER_ARTIFACT_ID',
			defaultValue: 'github-analytics-stub-runner-boot',
			description: "Artifact Id for Stub Runner used by tests"
		)
		string(
			name: 'STUBRUNNER_VERSION',
			defaultValue: '0.0.1.M1',
			description: "Artifact Version for Stub Runner used by tests"
		)
	}
	stages {
		stage("Build and Upload") {
			steps {
				checkout([
					$class: 'GitSCM',
					branches: [
						[name: env.TOOLS_BRANCH]
					],
					extensions: [
						[$class: 'WipeWorkspace'],
						[$class: 'RelativeTargetDirectory', relativeTargetDir: "${env.WORKSPACE}@tools"]
					],
					userRemoteConfigs: [
						[credentialsId: env.GIT_CREDENTIAL_ID, url: env.TOOLS_REPOSITORY]
					]
				])

				script {
					env.CF_TEST_USERNAME = env.CF_TEST_USR
					env.CF_TEST_PASSWORD = env.CF_TEST_PSW
					env.CF_STAGE_USERNAME = env.CF_STAGE_USR
					env.CF_STAGE_PASSWORD = env.CF_STAGE_PSW
					env.CF_PROD_USERNAME = env.CF_PROD_USR
					env.CF_PROD_PASSWORD = env.CF_PROD_PSW

					env.DEPLOY_TO_PROD = input message: '', parameters: [
						choice(
							name: 'Deploy to prod?',
							choices: 'no\nyes',
							description: 'Choose "yes" if you want to deploy this build to production'
						)
					]
				}

				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/build_and_upload.sh
				'''
			}
		}

		stage("Deploy to test") {
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/test_deploy.sh
				'''
			}
		}

		stage("Tests on test") {
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/test_smoke.sh
				'''
			}
		}

		stage("Deploy to test latest prod version") {
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/test_rollback_deploy.sh
				'''
			}
		}

		stage("Tests on test latest prod version") {
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/test_rollback_smoke.sh
				'''
			}
		}

		stage("Deploy to stage") {
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/stage_deploy.sh
				'''
			}
		}

		stage("End to end tests on stage") {
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/stage_e2e.sh
				'''
			}
		}

		stage("Deploy to prod") {
			when {
				environment name: 'DEPLOY_TO_PROD',
				value: 'yes'
			}
			steps {
				sh """
				# https://issues.jenkins-ci.org/browse/JENKINS-28335
				git config --local credential.helper cache
				echo 'protocol=https\nhost=github.com\nusername=${GIT_USR}\npassword=${GIT_PSW}\n\n' | git credential approve
				git tag prod/\${PIPELINE_VERSION}
				git push --tags
				"""

				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/prod_deploy.sh
				'''
			}
		}

		stage("Complete switch over") {
			when {
				environment name: 'DEPLOY_TO_PROD',
				value: 'yes'
			}
			steps {
				sh '''#!/bin/bash
				${WORKSPACE}@tools/common/src/main/bash/prod_complete.sh
				'''
			}
		}
	}
	post {
		always {
			junit '**/surefire-reports/*.xml,**/test-results/**/*.xml'
		}
	}
}
