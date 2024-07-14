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
    ```sh
    git clone https://github.com/shreyankadev/FileParserApplication.git
    cd file-parser-application
    ```

2. **Update `application.properties`:**
    Configure the `application.properties` file located in `src/main/resources/` to set the import folder path:
    ```properties
    import.folder.path=src/main/resources/files/
    ```

3. **Build the project:**
    ```sh
    mvn clean install
    ```

4. **Run the application:**
    ```sh
    mvn spring-boot:run
    ```

5. **Access H2 Console:**
    - URL: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - User Name: `sa`
    - Password: *(leave it empty)*

### Endpoints

#### Job Endpoints



### Example Requests

