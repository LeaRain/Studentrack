# Studentrack

Studentrack is an application for time tracking for students and lecturers. 
The idea is based on an interface for time tracking for the own success of different courses during studies.
Studentrack delivers a plattform to achieve that goal: Lecturers create their modules, students enroll in them.   
In addition, their grades as a measuring value for their success can be used. 

## Technology Stack
Studentrack is build with Java, Spring Boot, Maven and MySQL:
* Java: version 14 or later
* Spring: 2.5.6 or later
* MySQL: 5.7 or later
* Maven: 3.8.4 or later

## Setup 

1) Clone the repository and switch into the cloned directory.

### Docker
Use the Dockerfile, build your containers, run the application. Done. 
In more detail (obviously only if you have installed Docker and the user you are currently using is part of the correct user group):

``docker build .``

``docker compose up``


### Without Docker, run it on bare metal
Please use a database like described in `application.properties` or update it accordingly. 

Just use maven for building and make sure to install `terminportal-backend-0.0.1-SNAPSHOT.jar` In more detail:

``mvn install:install-file -Dfile=terminportal-backend.jar -DgroupId=sw.helblingd -DartifactId=terminportal-backend -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true``

``mvn clean package``

``mvn spring-boot:run``

## REST API
Studentrack has a REST API for (mostly) reading data. 
The idea is to register with a mail address and an organization and get access to lots of data.