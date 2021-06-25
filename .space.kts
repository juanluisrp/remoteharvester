/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Build, test and install project artifacts") {
    
    container(displayName = "Run mvn install", image = "maven:latest") {
        // url of a Space Packages repository
        env["REPOSITORY_URL"] = "https://maven.pkg.jetbrains.space/geocat/p/jrc-inspire-portal/maven"

        shellScript {
            content = """
            	echo === Build artifacts... ===
	            mvn -B clean install -DskipTests
                echo === Run the tests... ===
                mvn -B tests
                echo === Publish artifacts... ===
                mvn -B deploy -s .space/settings.xml \
                    -DskipTests \
                    -DrepositoryUrl=${'$'}REPOSITORY_URL \
                    -DspaceUsername=${'$'}JB_SPACE_CLIENT_ID \
                    -DspacePassword=${'$'}JB_SPACE_CLIENT_SECRET
            """
        }
    }
    
    job("Build and push Docker") {
        docker {
            build {
                context = "."
                file = "./Dockerfile"
                labels["vendor"] = "GeoCat B.V."
            }

            push("geocat.registry.jetbrains.space/p/jrc-inspire-portal/docker/myimage") {
                tags("\$JB_SPACE_GIT_BRANCH", "\$JB_SPACE_GIT_BRANCH-\$JB_SPACE_EXECUTION_NUMBER")
            }
        }
	}
    

}
