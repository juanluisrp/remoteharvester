/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Build, test and install project artifacts") {
    
    container(displayName = "Run mvn install", image = "maven:latest") {
        shellScript {
            content = """
	            mvn clean install
            """
        }
    }

}
