# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Time tracker backend application built with Spring Boot 4.0.1 and Java 21. Uses PostgreSQL with Flyway migrations, Spring Security for authentication, and Spring Data JPA for data access.

**Tech Stack:**
- Java 21
- Spring Boot 4.0.1
- Spring Web MVC (REST API)
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway (database migrations)
- Lombok
- Gradle (build tool)

## Common Commands

### Build and Run
```bash
./gradlew build                    # Build the project
./gradlew bootRun                  # Run the application
./gradlew clean build              # Clean and build
```

### Testing
```bash
./gradlew test                     # Run all tests
./gradlew test --tests ClassName   # Run a specific test class
./gradlew test --tests ClassName.methodName  # Run a specific test method
```

### Other Tasks
```bash
./gradlew tasks                    # List all available Gradle tasks
./gradlew dependencies             # Show dependency tree
./gradlew bootJar                  # Create executable JAR
```

## Architecture

### Package Structure
- Base package: `cl.pablovillarroel.timetracker`
- Standard Spring Boot layered architecture expected:
  - Controllers (REST endpoints)
  - Services (business logic)
  - Repositories (data access with Spring Data JPA)
  - Models/Entities (JPA entities)
  - DTOs (data transfer objects)
  - Configuration classes

### Database
- **Database**: PostgreSQL
- **Migrations**: Flyway migrations located in `src/main/resources/db/migration/`
- Migration naming convention: `V{version}__{description}.sql` (e.g., `V1__create_users_table.sql`)

### Configuration
- Application properties: `src/main/resources/application.properties`
- Currently minimal configuration; database connection details will need to be added

### Key Dependencies
- **Lombok**: Used for reducing boilerplate (getters/setters/constructors)
  - Requires annotation processing enabled in IDE
- **Spring Security**: Authentication and authorization configured
- **Validation**: Bean validation with Jakarta Validation API

### Testing
- Test framework: JUnit 5 (JUnit Platform)
- Spring Boot test starters available for:
  - Web MVC testing (`@WebMvcTest`)
  - Security testing (`@WithMockUser`, etc.)
  - JPA testing (`@DataJpaTest`)
  - Integration testing (`@SpringBootTest`)
