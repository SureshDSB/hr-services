# hr-application

This microservice serves as backend services for hr 


### Postman
The Postman collection is at [*PostmanCollection*](NPHC.postman_collection.json)

### Integration Tests

mvn -Dtest=com.nphc.hr.HrServiceApplicationTests test

## How to start the application in the local environment

### Start up
mvn spring-boot:run

#### logs
The logs are captured in the logs directory (${project.dir}/logs/) with name: hr-application.log




## How to start the application in the Docker environment
### Start up
mvn spring-boot:run
