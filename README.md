# awesome-pizza microservice

## Swagger
Link:
```
http://localhost:8080/swagger-ui/index.html
```

## Description
Application to manage a Pizzeria orders

5 APIs -> 2 to manage user actions and 3 to manage chef actions.

## Technical Details
- Java 21
- Spring Boot 4.1.2
- Maven 3.10.0
- Docker 24.0.6

## How to Start the Application
1. Load the docker-compose.yaml in "awesome-pizza-docker" or start the container if already created
2. Execute the command: 'maven clean install' to generate the jar
3. Start the jar with the command: java -jar "target\awesome-pizza-be-0.0.1-SNAPSHOT.jar" --spring.profiles.active=local
4. Access Swagger to test the APIs
