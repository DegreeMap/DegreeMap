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

### Setting Up Secrets
You will need to manually set up RSA public and private keys for encoding/decoding JWTs. You can generate your RSA public and private keys using OpenSSL.

Using OpenSSL:
- Create a `certs` directory in `resources` and navigate to it:
    ```bash
    cd src/main/resources/certs
    ```
- Run these commands in `certs`:
    ```
    openssl genrsa -out keypair.pem 2048
    openssl rsa -in keypair.pem -pubout -out publicKey.pem 
    openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out privateKey.pem
    rm keypair.pem
  ```
- You should end up with both a `privateKey.pem` and `publicKey.pem` file in your `certs` directory.

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