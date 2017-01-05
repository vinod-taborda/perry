# CWDS Security API - Perry

The CWDS Security API - Perry provides authentication and authorization services for CWDS Digital Services

## Documentation

The development team uses [Swagger](http://swagger.io/) for documenting the API.  
NOTE : At this time there is not a publicy available link to the documentation, a link will be provided as soon as one is available.

## Configuration

The CWDS API currently utilizes the CMS database for authorization information.

1. DB2 - CWDS CMS database

In order for Perry to successfully connect to the above database the following environment variables are required to be set:

- DB_CMS_USER -- the CWDS CMS database username
- DB_CMS_PASSWORD -- the CWDS CMS database password
- DB_CMS_JDBC_URL -- the CWDS CMS database URL in Java Database Connectivity format
- DB_CMS_SCHEMA -- the CWDS CMS database schema the tables belong to.

The Docker env-file option provides a convenient method to supply these variables. These instructions assume an env file called .env located in the current directory. The repository contains a sample env file called env.sample.

Further configuration options are available in the file config/perry.yml.

## Installation

### Prerequisites

1.  Postgres 9.x

### Using Docker

Perry is available as a Docker container on Dockerhub:

    https://hub.docker.com/r/cwds/perry/

Run the application with Docker using a command like this:

    % docker run --env-file=.env -p 8080:8080 cwds/perry

## Development Environment

### Prerequisites

1. Source code, available at [GitHub](https://github.com/ca-cwds/perry)
1. Java SE 8 development kit
1. DB2 Database
1. Docker ( if running a Database Docker Container )

### Database 

#### Docker - DB2
A [Docker Image](https://hub.docker.com/r/cwds/db2/) with DB2 is available to develop against.  The database server running in the container does not contain a database, to create one attach the container and create the database:
    
    % docker attach container_name
    % su - db2inst1
    % db2 create database DB0TDEV using CODESET ISO-8859-1 TERRITORY US

### Development Server

Use the gradlew command to execute the run task:

    % ./gradlew run

This will run the server on your local machine, port 8080.

### Unit Testing

Use the gradlew command to execute the test task:

    % ./gradlew test

### Integration Testing
Tests that access the database utilize the src/test/resources/hibernate.cfg.xml configuration file. Edit this file to utilize a local testing database.

Use the gradlew command to execute the test task:

    % ./gradlew integrationTest
    
### Commiting Changes

Before commiting changes to the reporsitory please run the following to ensure the build is successful.

    % ./gradlew clean test integrationTest javadoc

### Building Docker Container

The continuous delivery server builds images for the container registry but developers can build local containers with
the following command:

    % docker build -t perry .

This results in a local docker image in a repository called api with the tag latest.
