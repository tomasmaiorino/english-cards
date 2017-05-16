It's a RESTful service responsible for two simple e-commerce scenarios.

In this application, there is an embedded application server and a database.

This application is running under Spring Boot. 


## Used Technologies

**1. Java version 8.**

**3. JPA / MONGO DB:** Mapping persistent entities in domains objects.

**6. Logback:** Generation of logs.

**7. Spring Data JPA:** It's used to generate part of the code of the persistence layer.

## Additional Technologies

** mongoimport --db test --collection known_words --file 20k.txt


**Database:** HSQLDB embeded in the application. The database is created during the startup of the application. I offer the file src/main/resources/data.sql where we have the script for insert the data in the tables of the database. This data are used in the components of the tests (mock) and they are disponible for the web pages. In the end of the execution is destroyed the database.

**Tests:** The tests are defined as use case of the Junit. The tests of rest services have: Spring Web MVC for mock of the web infrastructure; JsonPath e hamcrest are used for access and assertions in the Json content. The tests have been made available in the structure: src/test/java.

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

###### To run the application the maven command:
$ mvn spring-boot:run

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

