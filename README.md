# **Tasks app - Backend**

## Table of contents
* [General info](#general-info)
* [User stories](#user-stories)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
* This project is created for FAIRit. program.
* Application consists of two repositories: backend ([Click here](https://github.com/AntoninaMJ/be-tasks)) and frontend ([Click here](https://github.com/AntoninaMJ/be-tasks-ui)).
* Used API: Allegro API ([Click here](https://developer.allegro.pl/)).
* Documentation: Swagger ([Click here](http://localhost:8080/swagger-ui.html#/)).
* Created by Antonina Marikhina ([Git Hub repositories](https://github.com/AntoninaMJ)).

## User stories
Application helps parents motivate child to get involved in chores.
There are two actors in the app: parent and child.
* Parent can:
- [x] modify an account (CRUD)
- [x] modify child account (CRUD)
- [x] view children information
- [x] view tasks
- [x] modify a task (CRUD)
- [x] add task to child
- [x] set done to task to do (allocate points for child)
- [x] buy present (remove points from child)
- [x] view transactions
* Child can:
- [x] view actual tasks
- [x] set done to task
- [x] choose preferred present (present list from Allegro API)

## Technologies
* JAVA 11
* Gradle
* Spring Boot (Security, DataJPA, Web)
* REST API
* Swagger
* Lombok
* PostgreSQL
* Hibernate
* JUnit 5
* Mockito
* TestRestTemplate
* Angular (TypeScript)
* HTML
* CSS
* GitHub Actions

## Setup:
Needed: Java, Gradle.