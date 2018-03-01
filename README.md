It's rest api that is used to provide end points to create, update and remove cards and contents. This application has been providing the content to a  [link website](https://tomasmaiorino.bitbucket.io/) that was created to help english students to improve their vocabulary and the grammar knowledge as well.

## Used Technologies

**1. Java version 8.**

**2. POSTGRES **

**3. Spring boot 1.5.4 **

**4. Maven **  Life cycle management and project build.

**5. Docker (Optional) ** Used container manager to create an application image and the containers.

## Considerations

**Tests:** The tests are defined as use case of the Junit. The tests have been made available in the structure: src/test/java.

**Integration Tests:** The integration tests are defined as use case of the Junit. The tests have been made available in the structure: src/it/java.

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
```$
psql -U postgres
```  
```$
create database english_cards;
```  
```$
\q
```  
5 - To exit from container.  
```$
quit
```  
6 - To create application image. (This steps excute the mvn clean install automatically)  
```$
docker build -t eng --build-arg branch_name=master .
```  
7 - To create application container and start it.  
```$
docker run --rm -it --link postgres -p 8080:8080 --name eng eng mvn spring-boot:run -Drun.arguments="--spring.profiles.active=<profile_name>"
```  

###### To run the integrations tests through docker run this command:
```$
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

#### Retrieve Card type.
```$
curl -i GET http://localhost:8080/api/v1/cards-type/{id}
```

#### Create card.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/cards -d "{\"cardType\": 2,\"name\": \"mirror\",\"imgUrl\": \"assets/img/cards/bathroom/mirror.jpg\", \"status\":\"ACTIVE\"}"
```

#### Update card.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/cards/{id} -d "{\"cardType\": 2,\"name\": \"mirror\",\"imgUrl\": \"assets/img/cards/bathroom/mirror.jpg\", \"status\":\"ACTIVE\"}"
```

#### Retrieve card.
```$
curl -i GET http://localhost:8080/api/v1/cards/{id}
```

#### Delete card.
```$
curl -i DELETE http://localhost:8080/api/v1/cards/{id}
```

#### Create content type.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/content-types -d "{\"name\": \"Grammar\", \"status\":\"ACTIVE\"}"
```

#### Update content type.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/content-type/{id} -d "{\"name\": \"Grammar\", \"status\":\"INACTIVE\"}"
```

#### Retrieve Content type.
```$
curl -i GET http://localhost:8080/api/v1/content-types/{id}
```

#### Retrieve Content type by content type name.
```$
curl -i GET http://localhost:8080/api/v1/content-types?q={contentTypeName}
```

#### Create content.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/contents -d "{\"name\": \"Grammar\", \"status\":\"ACTIVE\", \"content\":\"\"}"
```

#### Update content.
```$
curl -i -H "Content-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X PUT http://localhost:8080/api/v1/contents/{id} -d "{\"name\": \"Grammar\", \"status\":\"ACTIVE\", \"content\":\"\"}"
```

#### Retrieve content.
```$
curl -i GET http://localhost:8080/api/v1/contents/{1}
```
