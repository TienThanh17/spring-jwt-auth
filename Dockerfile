 FROM openjdk:21-jdk
 RUN gradle clean build
 WORKDIR build/
 COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
 EXPOSE 8888
 ENTRYPOINT ["java", "-jar", "app.jar"]





