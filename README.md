# LPG Home exercise 

## The Exercise
The aim of this exercise is to write a tourism pass management solution. The system should allow
customers to add, cancel, and renew a pass; it should also allow tourist attractions to verify if a pass is
active. There is no need to worry about payment of passes. The system will be a RESTful application
written in Java 8 (or later), using Spring or some other framework of your choice. Usage of Maven and
other build tools are permitted.

### Getting Started

The repository has a generic Spring boot RESTApi application with Mysql at the backend. 
API specification for the RESTApi's can be found here [Docs](https://documenter.getpostman.com/view/6577107/SzRxVVy5?version=latest)

### Assumtions Made

Some assumtions that were made on the business logic are:

* The Vendor will register using POST /v1/vendor
* The Customer will register using POST /v1/customer
* Vendor will create passes, for customers to add with pass_length and pass_city. Vendor can also delete a pass and validate a pass.
* Customer can get all valid passes and send a post to add passes to their account
* Customers have the option to renew passes by providing a pass length and cancel a given pass(Removes the pass from customers account)
* Passes become available when vendors add them and are not expired, expired pass dont show up in /allpasses


## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org)

For database, you will need a mysql database with an empty Schema "lpgdemo" (This can be configured in application.properties) 


## Build Instructions
1. Clone repository
2. Import maven project in IDE
3. Run mvn build command to compile the project 

## Deploy Instructions Using Docker
Make sure docker is installed in the system
1. Go to project base directory (/lpgdemo)
2. Run the following command to build a docker image:
	docker build -t 'repo:imageTag' .
3. Run the built docker image with the following command and use local database
	docker run -d --network host 'repo:imageTag'

## Running the application locally in IDE

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.lpgdemo.demo.DemoApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

To change the database values refer application.properties under resources used with executing the jar files as command line parameters

### Design & Considerations
* The application is modelled so as to be scalable and modular with testing examples
* Its dockerized and can be depolyed as a container on any containerized platform



## Thank you for your consideration and time in reviewing this application. 



