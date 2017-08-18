It's a application which helps the english student to learn new words.

This application is running under Spring Boot.
This applications uses the Oxford service api (https://developer.oxforddictionaries.com/) therefore it is necessary to have an account on oxford service and use their OXFORD_SERVICE_API_ID and OXFORD_SERVICE_APP_KEY. 


## Used Technologies

**1. Java version 8.**

**2. JPA / MONGO DB:** Mapping persistent entities in domains objects.

**3. Spring Data JPA:** It's used to generate part of the code of the persistence layer.

## Additional Technologies

**Database:** For the initial load for the known_word database is based on the known_words.json file. If some problem occur during initial load, it can be made manually e.g: mongoimport --db test --collection known_words --file known_words.json

**Tests:** The tests are defined as use case of the Junit. The tests have been made available in the structure: src/test/java.

**Spring Boot:** Technology used for create a embeded enviroment of the execution, I used this technology for simplify the use of the Spring and for controle the scope of the database. In the file src/main/resources/application.yml have properties of the Spring Boot for the project.

**Maven:** Life cycle management and project build.

## Considerations

The integration of the pages with the data occurs asynchronously, always making access to REST services available.

## Usage In Local Machine

###### Pré-requisitos
```
JDK - Java version 1.7.

Any Java IDE with support Maven.

Maven for build and dependecies.


###### After download the fonts, to install the application and test it execute the maven command:
$ mvn clean install

###### To only test the application execute the maven command:
$ mvn clean test

###### To run the integrations tests excute the maven command:
$ mvn verify -DskipItTest=false  -P it -Doxford.service.api.id=OXFORD_SERVICE_API_ID -Doxford.service.app.key=OXFORD_SERVICE_APP_KEY

###### To run the application the maven command:
$ mvn spring-boot:run -Dspring.profiles.active=profile -Doxford.service.api.id=xxx -Doxford.service.app.key=xxx

###### To test the find products by id, open the browser of your preference and type it:
http://localhost:8080/product/1

###### To test the find products by catalog service, open the browser of your preference and type it:
http://localhost:8080/product/cat/Store

###### To test find list service by id service, open the browser of your preference and type it:
http://localhost:8080/order/1

###### To test the find orders by sku id service, open the browser of your preference and type it:
http://localhost:8080/order/sku/1

###### To test the find all orders service, open the browser of your preference and type it:
http://localhost:8080/order/all

###### To test the create order service, tpye it:
curl -i -H "Content-Type:application/json" -H "Accept:application/json" -X POST http://localhost:8080/order -d "{\"commerceItems\": [{\"sku\": {\"id\": 3},\"quantity\": 12,\"unitValue\": 12}],\"status\": \"SUBMITTED\",\"paymentStatus\": \"CREATED\",\"totalAmount\": 26}

##### To update the order, type it:
curl -i -H "Content-Type:application/json" -H "Accept:application/json" -X PUT -d "{\"id\":4, \"commerceItems\": [{\"sku\": {\"id\": 1},\"quantity\": 12,\"unitValue\": 12}],\"status\": \"APPROVED\",\"paymentStatus\": \"CREATED\",\"totalAmount\": 21}" http://localhost:8080/order