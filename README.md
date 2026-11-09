# awesome-pizza microservice

## Swagger
Link:
```
http://localhost:8080/swagger-ui/index.html
```

## Descrizione
Applicazione per gestire gli ordini di una pizzeria.

5 API -> 2 per gestire le azioni dell'utente e 3 per gestire le azioni del pizzaiolo.

## Dettagli tecnici
- Java 21
- Spring Boot 4.1.2
- Maven 3.10.0
- Docker 24.0.6

## Come avviare l'applicazione
1. Caricare il docker-compose.yaml in "awesome-pizza-docker" o avviare il container se già creato
2. Eseguire il comando: 'maven clean install' per generare il jar
3. Avviare il jar con il comando: java -jar "target\awesome-pizza-be-0.0.1-SNAPSHOT.jar" --spring.profiles.active=local
4. Accedere a Swagger per testare le API
