# race management application

## Project Overview

Showcase of QCRS architecture, demonstrating the integration of a Spring Boot backend services, Angular frontend, 
Cassandra database and Postgres Database, all managed and deployed using Docker Compose.

## Technologies Used

**Backend**:
- Spring Boot 3.1.5 (with Actuator, JPA, Kafka, and Cassandra) 
- PostgreSQL (for command service data) 
- Cassandra (for query service data) 
- Apache Kafka (for event streaming) 

**Frontend**:
- Angular (with Angular CLI) 
- Angular Material (for UI components) 
- JWT (for authentication and role-based authorization) 

**Deployment**:
- Docker and Docker Compose (tested on Docker Desktop for Windows)

## Prerequisites

- Docker Desktop for Windows (which includes Docker Compose)

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Filip31337/race_management_app.git
cd race_management_app
```

### 2. Build images
- Navigate to the root directory (make sure the file docker-compose.yml is located in current directory)
```bash
docker-compose build --no-cache
```
Run docker compose to build images

### 3. Run images
Run docker compose in detached mode and wait for each service to start up, average time is 70 to 90 s on 
modern machine, migration from postgres to cassandra is executed at 85th second after command startup.

```bash
docker-compose up -d
```

### 4. Access application using browser
Once running, access the Angular client at http://localhost:80 (port 80) and use the provided credentials or 
mock tokens for Administrator or Applicant roles.
```bash
http://localhost/
```

Mocked credentials:
- admin@admin.com:admin
- user@user.com:user
- 
### 5. Possible problems
- EOL characters on nginix config, should be LF for linux, but windows might declare it CRLF after git clone, the fix is to open the config in any text editor and save as LF EOL character based