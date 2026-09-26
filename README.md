# NexusGrade 🎓

NexusGrade is a modern, web-based grade and attendance management system built to streamline academic tracking and reporting for educational institutions. 
Designed with scalability, security, and developer ergonomics in mind, the platform uses a robust Spring Boot backend paired with Thymeleaf templates.
The application is built with Java and Spring Boot and is designed around a secure, role-based architecture for managing academic information.

## Features

- Student management
- Teacher and staff management
- Class management
- Class sessions/periods management & Time table
- Assignment creation and submission
- Grade management and calculations
- Academic reporting
- Student and teacher dashboards
- Role-based access control
- Authentication and authorization
- Secure handling of application and database access
- Docker-based deployment

## Technology

- **Java 25**
- **Spring Boot**
- **Spring MVC**
- **Spring Security**
- **Spring Data JPA / Hibernate**
- **Thymeleaf**
- **HTML, CSS & JavaScript**
- **Bootstrap**
- **MariaDB**
- **Docker**
- **Nginx**
- **Git**

## Architecture

NexusGrade follows a layered Spring Boot architecture with separate responsibilities for web requests, business logic, data access, security, and persistence.

The application uses MariaDB for persistent data storage and can be deployed using Docker containers. Nginx can be used as a reverse proxy for production deployments, including HTTPS termination.

## Deployment

NexusGrade can be containerized and deployed using Docker.

The typical deployment consists of:

- NexusGrade application container
- MariaDB database container
- Nginx reverse proxy

Environment-specific configuration and credentials should be supplied through environment variables or external configuration rather than being committed to the repository.

## Configuration

NexusGrade uses environment-specific configuration for settings such as:

- Database connection details
- Database credentials
- Application profile (Development/Production)
- Security configuration
- Other deployment-specific values

Sensitive configuration files and credentials should not be committed to source control.

## Project Status

NexusGrade is an actively developed project.

Features, architecture, and implementation details may change as development continues.

## License

NexusGrade is licensed under the **PolyForm Strict License 1.0.0**.

The license permits use of the software under the conditions defined by the license, but does not grant general permission to modify or redistribute the software.

See the [`LICENSE`](LICENSE) file for the complete license terms.

Copyright © 2026 Sphelele