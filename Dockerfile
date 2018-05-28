FROM openjdk:8-alpine
ADD target/auth-service-0.0.1-SNAPSHOT.jar auth-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","auth-service-0.0.1-SNAPSHOT.jar"]
