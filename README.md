Customer OnBoarding API
Frameworks Used
Java 21, Spring Boot 3.5.x, Maven 3.9. For removing bolier plate code --- Lombok, --Mapstruct. API Documentation - OpenApi 3.0.3. Container- Docker Testing - Junit jupiter, Mockito, MockMvc No checkstyles files used

Considerations and assumptions
API first approach was chosen
Due to lack of time the developer could not fix the issue with Dockerising but still added the file for reference
No plugins used to generate any class or methods
Code setup and run(if docker is installed)

Clone the project from gitHub
Navigate to project root folder and execute mvn clean install on the project root directory
Navigate to \config-server\target, execute java -jar -.jar
Navigate to \registry-server\target, execute java -jar -.jar
Navigate to \onboarding\target, execute java -jar -.jar
Navigate to \notification\target, execute java -jar -.jar

Wait untill you see "Response 200 received from servive-registry in Onboarding app logs" so that the services are registered for discovery
UserName and password to access the respective API are defined in the application.properties(config-server/resources/repo/) as security is enabled
API End points to test
Service registry- http://localhost:8761/login

How To Test

OnBoard customer- POST http://localhost:9093/api/v1/customer-onboarding 

form-data:  - key =onboardRequest
value={
"fullName": "John Doe",
"gender": "MALE",
"dateOfBirth": "1990-01-01",
"email": "john.doe@example.com",
"phoneNumber": "+31612345678",
"nationality": "Dutch",
"address": {
"streetName": "Main Street",
"houseNumber": "123A",
"city": "Amsterdam",
"postalCode": "1011AB",
"country": "Netherlands"
},
"bsnNumber": "123456789"
}
Content type : application/json

key: passport
value: choose a sample passport

To check the DB entry spring security needs to be disabled(not succesful with security configuration to allow DB console)

Packaging structure
Considering microservice architecture functional packaging is considered so that it can be deployed as seperate artifacts

Production ready considerations:

1. Spring boot application runs with active profiles hence properties per environment is placed and the prod profile is configured

2. Externalised the application properties. So that actuator/refresh can be used to load the properties without having to do deployment

3. Spring security is enabled with different credentials for prod with roles for different endpoint. Roles can also be configured in tables and can be made run time rather than hardcoding
4. Actuator endpoints are made available to capture health and metrics
5. Passwords are hardcoded in the code or property file as there is no vault integration done and in real time it can be fetched from secure vaults

TODO/Scope of improvement:

1. Fix issues after dockerising so that in memory DB could have been avoided
2. SSL certificate to be added to enhance security (https)
3. Mock SMPTP is used to verify email is triggered
4. Correct the security configuration