# Automated system for FLDSMDFR food order and delivery
## Build instructions
### Prerequisites 
Make sure to have java 17+ (jdk17+) and maven 3.8+ installed.
````
java --version
````
Should give output like this
````
java 17.0.2 2022-01-18 LTS
Java(TM) SE Runtime Environment (build 17.0.2+8-LTS-86)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.2+8-LTS-86, mixed mode, sharing)
````
And also for maven
````
mvn --version
````
````
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
````

Also have postgres running (version no lower than 13)
````
postgres -V
````
Should give something like
````
postgres (PostgreSQL) 13.3
````
Also postgres should be running on port 5432 by default and have database with name `FLDSMDFR` created.

Db configs for project can be changed in file `src/main/resources/application.properties`
````
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/FLDSMDFR #this is the pg url (port and db name)
spring.datasource.username=postgres #pg username
spring.datasource.password=password #pg pass
spring.jpa.hibernate.ddl-auto=create
server.port=8080 #app http port
````

### Build command
````
mvn clean install 
````
Should be run in project root folder
### Run command 
````
java -jar target/FLDSMDFR-0.0.1-SNAPSHOT.jar
````
