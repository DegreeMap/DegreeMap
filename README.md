# DegreeMap
A web application that helps you visualize and plan your college course schedule through customizable flowcharts.

### Running Front-end:
To run the website locally, run this command in the root directory: 
```
npm run dev
```
Then navigate to the provided local link (i.e. http://localhost:3000).

### Running Database:
To run the database for the application, you can run this command in the root directory:
```
docker-compose up -d
```

### Running Back-end:
To run the backend server for the application you MUST have the database running, then you may either run the DegreeMapApplication.java file using some IDE or you can run this command in the root directory:
```
./mvnw spring-boot:run
```

### Dependencies:
Additional dependencies can be found in the pom.xml file, although you won't have to download anything for those ones you'll just probably need to know what they are.
- Java 21
- Maven 3.8.7
- Spring Boot 3.2.5
- MySQL 8.4.0
- Docker Desktop