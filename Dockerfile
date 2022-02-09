FROM maven:3.8.4-openjdk-17
VOLUME /tmp
COPY target/Assignment2-1.0-SNAPSHOT-jar-with-dependencies.jar Assignment2.jar
ENV MAVENINTGR_MAVENHOME=/usr/share/maven
CMD ["java","-jar","Assignment2.jar"]
EXPOSE 8081