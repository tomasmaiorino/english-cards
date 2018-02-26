It's a application which helps the english student to learn new words. By using this application you will be able to search for more than one word simultaneously.
This is an initial version and will not be finished. I'm currently working on the branch v3 which will have new features for the english learning also a better UX.

In order to access the actual branch you can access this url: https://fathomless-tundra-22713.herokuapp.com/ and to search for some new words meanings.

This application is running under Spring Boot.
This applications uses the Oxford service api (https://developer.oxforddictionaries.com/) therefore it is necessary to have an account on oxford service and use their OXFORD_SERVICE_API_ID and OXFORD_SERVICE_APP_KEY.

## Create docker image
$ docker build -t <image_name> --build-arg branch_name=<parameter_value> .

## Create docker container
$ docker run -it --rm --link <container_image_name> -p 8080:8080 --name <container_name> <image_name>  mvn spring-boot:run -Drun.arguments="--spring.profiles.active=local"

## Used Technologies

**1. Java version 8.**

**2. POSTGRES / MONGO DB:** Mapping persistent entities in domains objects.

## Additional Technologies

**Database:** For the initial load for the known_word database is based on the known_words.json file. If some problem occur during initial load, it can be made manually e.g: mongoimport --db test --collection known_words --file known_words.json

**Tests:** The tests are defined as use case of the Junit. The tests have been made available in the structure: src/test/java.

**Integration Tests:** The integration tests are defined as use case of the Junit. The tests have been made available in the structure: src/it/java.

**Spring Boot:** Technology used for create a embeded enviroment of the execution, I used this technology for simplify the use of the Spring and for controle the scope of the database. In the file src/main/resources/application.yml have properties of the Spring Boot for the project.

**Maven:** Life cycle management and project build.

**Docker** Used container manager to create an application image and the containers.

## Considerations

The integration of the pages with the data occurs asynchronously, always making access to REST services available.

## Usage In Local Machine

###### Pré-requisitos

JDK - Java version 1.8.

Docker latest version. (For docker installation)

Maven for build and dependecies. (For not docker installation)

### Using Docker

1 - To install postgres container.  
```$
docker pull postgres
```  
2 - To create container.  
```$
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=initdb -d postgres
```  
3 - To access the postgres container.
```$
docker exec -it postgres /bin/sh
```  
4 - To postgres and to create the database english_cards.  
```
psql -U postgres
```  
```
create database english_cards;
```  
```
\q
```  
5 - To exit from container.  
```
$ quit
```  
6 - To create application image. (This steps excute the mvn clean install automatically)  
```$
docker build -t eng --build-arg branch_name=master .
```  
7 - To create application container and start it.  
```$
docker run --rm -it --link postgres -p 8080:8080 --name eng eng mvn spring-boot:run -Drun.arguments="--spring.profiles.active=container"
```  

###### To run the integrations tests through docker run this command:
```sh
docker run --rm -it --name eng_it eng mvn verify -DskipItTest=false -Drun.arguments="--spring.profiles.active=it"
```

### Not using Docker

Any Java IDE with support Maven.

Maven for build and dependecies.

###### After download the fonts from [link github](http://github.com/tomasmaiorino), to install the application and test it execute the maven command:
```$
mvn clean instal
```

###### To only test the application execute the maven command:
```$
mvn clean test
```

###### To run the integrations tests excute the maven command:
```$
mvn verify -DskipItTest=false  -P it -Doxford.service.api.id=OXFORD_SERVICE_API_ID -Doxford.service.app.key=OXFORD_SERVICE_APP_KEY
```

###### To run the application the maven command:
```$
mvn spring-boot:run -Dspring.profiles.active=profile -Doxford.service.api.id=xxx -Doxford.service.app.key=xxx
```

###### Service's call examples:

#### Create card type.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/cards-type -d "{\"name\": \"Kicthen\",\"imgUrl\": \"assets/img/cards/kitchen/kitchen.jpg\",\"status\":\"ACTIVE\"}"
```

#### Update Card type.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/cards-type/{id} -d "{\"name\": \"Kicthen\",\"imgUrl\": \"assets/img/cards/kitchen/kitchen.jpg\",\"status\":\"ACTIVE\"}"
```

#### Create card.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/cards -d "{\"cardType\": 2,\"name\": \"mirror\",\"imgUrl\": \"assets/img/cards/bathroom/mirror.jpg\", \"status\":\"ACTIVE\"}"
```

#### Update card.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/cards/{id} -d "{\"cardType\": 2,\"name\": \"mirror\",\"imgUrl\": \"assets/img/cards/bathroom/mirror.jpg\", \"status\":\"ACTIVE\"}"
```

#### Create content type.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/content-types -d "{\"name\": \"Grammar\", \"status\":\"ACTIVE\"}"
```

#### Update content type.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/content-type/{id} -d "{\"name\": \"Grammar\", \"status\":\"INACTIVE\"}"
```

#### Create content.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/contents -d "{\"name\": \"Grammar\", \"status\":\"ACTIVE\", \"content\":\"\"}"
```

#### Update content.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/contents/{id} -d "{\"name\": \"Grammar\", \"status\":\"ACTIVE\", \"content\":\"\"}"
```
