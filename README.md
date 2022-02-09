# Assignment 2: CI-Server (DD2480)

In this assignment we were tasked to write a CI-server that automatically builds and test a codebase when a commit is performed.

## Summary

### Program

The CI-server is deployed on Heroku (https://ci-servergroup5.herokuapp.com/) where the main codebase is located. The main-routine runs on the `/ci` endpoint. A github webhook on this repository is connected to the heroku endpoint `/ci` so when a commit is made here, a payload is sent to the Heroku-server. The server, which runs in a Docker container, takes the payload information and uses the `branch` name and `clone_url` from the payload to clone the latest version of repository on that branch to the server machine. Then it compiles, builds and tests it using maven. The build and test logs are gathered and sent to the email address thats associated with the commit. 

We've also implemented a seperate CI-workflow for our CI-server code to also take advantage of automatic deployment once a commit is made to the main branch.

#### Endpoints
- **POST**, `/ci` : Endpoint that accepts GitHub payload from webhook, where the payload needs to contain reference to `branch`and `clone_url`. 
- **GET**, `/` : Startpage for endpoint that only provides a informative tex and no other functionality.
- **GET**, `/buildHistory` : Provides a HTML-page of the build-history of this repository and information and URL for each respective commit.

### Tooling

- **Programming Language:** Java was used due to all members having experience with it and its support for testing. 
- **Project Updates:** GitHub built-in _Projects_ tool was used with an active Kanban-board. You can find this board connected to this repo.
- **Build tools:** Maven.
- **Testing:** JUnit.
- **CI:** GitHub Actions. Runs all specified JUnit tests and build before integration.
- **Server:** Heroku and Docker for containerization

---


## Group Members:
- Gabriel Acar (Gabriel-Acar)
- Elias Bonnici (elibon99)
- Gustaf Halvardsson (gustafvh)
- Alexander Krantz (Klako)
- Oscar Spolander (Carnoustie)

## Contributions 
(# = IssueNumber on Github if applicable)

### Gabriel Acar
- Task

### Elias Bonnici
- Task

### Gustaf Halvardsson
- Task

### Alexander Krantz
- Task

### Oscar Spolander
- Task


## How to run the code

Thanks to the configuration of the `pom.xml` file, you can run, build and test the code in the most easy manner with your integrated IDE (like Intellij for ex.). 

In order to test the project, run `mvn exec:java`

In order for the maven compile and test functionalities to function correctly, one of the following actions must be taken. 
1. Set the `JAVA_HOME` environment variable to the location of a JDK.
2. Set the `MAVENINTGR_JAVAHOME` environment variable to the location of a JDK.
3. Copy the `user.properties.template` to `user.properties`and set the `mavenintgr.java_home` property to the location of a JDK


