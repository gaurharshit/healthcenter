# Healthcenter
Provides a health status management for multiple applications with health check URL

# Features
Register new applications with name and health check URL<br>
Monitor status of the applications with manual trigger or customised time interval checks<br>
Deregister the application

## Prerequisite
1. Maven 3.5.3 or above
2. Java 11
## How To Run

### Steps to build and run
1. `mvn clean install`
2. `mvn spring-boot:run`
### Optional docker run
mvn clean install will create a docker image with the name healthcenter:1.0.0. You can run and play with the application using the following command<br>
`docker run -p 8080:8080 -p 8081:8081 healthcenter:1.0.0`
## Application Access
### Healthcenter GUI
By default the application is running on port 8080
GUI should be accessible at http://localhost:8080
### H2 database GUI
H2 database is manually started at port 8081
H2 database should be accessible at http://localhost:8081
#### Login Details
JDBC URL: jdbc:h2:file:./health-center <br>
User Name: sa <br>
Password: password <br>
