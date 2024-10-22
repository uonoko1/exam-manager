# exam-manager
The exam-manager is a service designed for managing student exam results, based on a microservices architecture. This repository is designed following Onion Architecture and Domain-Driven Design (DDD), and it was created as a subject for a [technical blog](https://daichisakai.net/blog/671692eb687be14a298dc33b) post I wrote.

## Technologies Used
- Programming Language: Scala 3.4.1
- Framework: Play Framework 3.0.4
- Database: MySQL 8.0 for production, H2 in-memory database for testing
- ORM: Play-Slick 6.1.0
- Dependency Injection: Guice
- ID Generation: ULID via Airframe ULID
- Functional Programming: Cats-core 2.6.1
- Testing:
  - ScalatestPlus Play 7.0.1 for Play framework tests
  - Mockito for mocking
  - Pekko (formerly Akka) for actor-based testing with Pekko-Testkit and Pekko-Actor
- Build Tool: sbt 1.9.9
- Database Evolutions: Play Evolutions for automatic database migration

## How to Start
There are three ways to run the application, depending on your setup. This guide provides instructions for VSCode users leveraging devcontainers, as well as alternative instructions for those using other IDEs or editors.

### 1. Writing Code (Development Container)
#### For VSCode Users:
If you're using VSCode, follow these steps to use devcontainers for writing code:

1. Install the Remote - Containers extension in VSCode.
2. Clone the repository and open it in VSCode.
3. When prompted, choose Reopen in Container. This will automatically build the development environment using the .devcontainer setup.
4. Once the container is built and running, you can write and edit code directly from within the container using VSCode.

Alternatively, you can open the Command Palette (Ctrl + Shift + P), search for "Dev Containers: Reopen in Container", and select it to start the container manually.

#### For Other IDEs or Editors:
If you are not using VSCode, follow these steps:

1. Ensure Docker and Docker Compose are installed on your machine.
2. Clone the repository and navigate to the root directory.
3. Run the following command to build and run the development container:
    ~~~bash
    docker compose -f .devcontainer/docker-compose.yml up --build
    ~~~
4. You can use any IDE or editor to write and edit the code. To connect to the container terminal, use:
    ~~~bash
    docker exec -it <container_name> /bin/bash
    ~~~

### 2. Running the Application in Development
Once you've written your code, you can run the application in a development environment using the following steps:

1. Navigate to the localEnv directory:
    ~~~bash
    cd localEnv
    ~~~

2. Build and run the development environment container:
    ~~~bash
    docker compose up --build
    ~~~
    This will start the development version of the application.

### 3. Running the Application in Production
To run the application in a production-like environment, follow these steps:

1. From the root directory of the repository, run:
    ~~~bash
    docker compose up --build
    ~~~
    This will start the production container, exposing the necessary ports.