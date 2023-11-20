## Build the application & run unit tests
1-  Open the command-line terminal in the project's top level directory and issue the following command

    mvn clean install
## Launch the application in performance test mode
1-  Open the command-line terminal in the project's top level directory and issue the following command (skip this step if the application is already built)

    mvn clean install
2- Run the following command 

    cd target && java -DperformanceTestMode=true -Dspring.profiles.active=production  -jar test-assignment-financing-0.0.1-SNAPSHOT.jar

Application logs can be monitored in target/application.log

