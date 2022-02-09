# Assignment2
Assignment2

## How to run
In order to test the project, run `mvn test`

In order for the maven compile and test functionalities to function correctly, one of the following actions must be taken. 
1. Set the `JAVA_HOME` environment variable to the location of a JDK.
2. Set the `MAVENINTGR_JAVAHOME` environment variable to the location of a JDK.
3. Copy the `user.properties.template` to `user.properties`and set the `mavenintgr.java_home` property to the location of a JDK

If the application is not started from a maven command, you also need to set either the `maven.home` property or the `MAVENINTGR_MAVENHOME` environment variable to a maven home directory.

In order to generate documentation, run `mvn javadoc:javadoc`