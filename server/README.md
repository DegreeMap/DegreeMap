# Back-end Documentation:

### Info:
TODO

### Running Database:
To run the database for the application, you can run this command in the server directory:
``` bash
docker-compose up -d
```
To close the database, either delete the docker container or run this command in the server directory:
``` bash
docker-compose down 
```

### Running Back-end:
To run the backend server for the application you MUST have the database running, then you may either run the DegreeMapApplication.java file using some IDE or you can run this command in the root directory:
``` bash
./mvnw spring-boot:run
```
To close the backend server, Ctrl + C in the terminal running the server.

### Back-end Dependencies:
Additional dependencies can be found in the pom.xml file, although you won't have to download anything for those ones you'll just probably need to know what they are.
- Java 21
- Maven 3.8.7
- Spring Boot 3.2.5
- MySQL 8.4.0
- Docker Desktop