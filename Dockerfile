FROM maven:3.8.4-openjdk-17
VOLUME /tmp
COPY . /app
WORKDIR /app
RUN mvn install
ENV MAVENINTGR_MAVENHOME=/usr/share/maven
CMD ["java","-jar","target/Assignment2-1.0-SNAPSHOT-jar-with-dependencies.jar"]
EXPOSE 8081