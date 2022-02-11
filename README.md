# Assignment 2: CI-Server (DD2480)

In this assignment we were tasked to write a CI-server that automatically builds and test a codebase when a commit is performed.

## Summary

### Program

The CI-server is deployed on Heroku (https://ci-servergroup5.herokuapp.com/) where the main codebase is located. The main-routine runs on the `/ci` endpoint. A github webhook on this repository is connected to the heroku endpoint `/ci` so when a commit is made here, a payload is sent to the Heroku-server. The server, which runs in a Docker container, takes the payload information and uses the `branch` name and `clone_url` from the payload to clone the latest version of repository on that branch to the server machine. Then it compiles, builds and tests it using maven. The build and test logs are gathered and sent to the email address thats associated with the commit. The compilation/test method is unit tested by running it on smaller maven projects. The notification method is tested by sending emails to a locally hosted smtp server and asserting that the correct information was received.

We've also implemented a seperate CI-workflow for our CI-server code to also take advantage of automatic deployment once a commit is made to the main branch.

#### Endpoints
- **POST**, `/ci` : Endpoint that accepts GitHub payload from webhook, where the payload needs to contain reference to `branch`and `clone_url`. 
- **GET**, `/` : Startpage for endpoint that only provides a informative tex and no other functionality.
- **GET**, `/buildHistory` : Provides a HTML-page of the build-history of this repository and information and URL for each respective commit.
- **GET**, `/build/{id}` : Provides a HTML-page for a specific build where the specific id can be found in the buildHistory endpoint. 

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
- Implement P3: Core CI feature 3 - Notification (#6, #14)

### Elias Bonnici
- Integrated build/compilation into CI pipeline (#13)
- Created database and functions to store and fetch buildHistory (#16) 

### Gustaf Halvardsson
- Add a thorough README to repository (#23)
- Create example program + tests to test CI-server workflow with (#17)
- CI-Server Setup (#2)
- Add Template Code (#1)

### Alexander Krantz

- Fixing runtime environment on Heroku server (#32) with Gustaf
- Add ability to customize maven home directory (#31)
- MavenIntegration test method not working (#27)
- P5: Generate code documentation (#24)
- Implement webhook validation check (#19)
- Implement a code testing function (#12)
- Initialize maven pom (#9)

### Oscar Spolander
- Implement a code fetching function (#11)
- Write Paragraph about our Team in the Essence standard. (#18)

## How to run the code

Since we are using docker you need to build the image first using `docker build -t assessment2 . ` when in the root folder of the project. Once it has finished building run the container using `docker run -p 8081:8081 -t assessment2`. Then the container will run the server.

You can use `mvn javadoc:javadoc` in order to generate the source documentation.
