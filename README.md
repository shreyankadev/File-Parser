# FileParserApplcation

## Overview
This project is a Spring Boot application that allows importing CSV and XLSX files, parsing them asynchronously, storing the parsed results in a database, and exposing REST APIs to interact with the jobs and results.

## Features
- Import CSV and XLSX files.
- Asynchronous file processing.
- Store parsed data in a database.
- REST API to create jobs, fetch job statuses, and retrieve parsed results.
- Chart visualization of parsed count of results against job ids.

## Technologies Used
- Spring Boot
- Spring Data JPA
- H2 Database (for development)
- Thymeleaf
- Chart.js
- JUnit and Mockito (for testing)

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven

### Setup

1. **Clone the repository:**

    git clone https://github.com/shreyankadev/File-Parser.git



2. **Update `application.properties`:**
    Configure the `application.properties` file located in `src/main/resources/` to set the import folder path:

    import.folder.path=src/main/resources/files/


3. **Build the project:**

    mvn clean install


4. **Run the application:**

    mvn spring-boot:run


5. **Access H2 Console:**
    - URL: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - User Name: `sa`
    - Password: *(leave it empty)*
	
6. ** Access chart:**
	- access url http://localhost:8080/

### Endpoints

Documentation of Endpoints available at https://documenter.getpostman.com/view/33816603/2sA3kPo4BL

