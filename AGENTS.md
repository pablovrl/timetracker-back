# AGENTS.md

This file provides guidance to AI coding agents working in this Spring Boot time tracker backend repository.

## Build, Test, and Lint Commands

```bash
./gradlew build                    # Build the project
./gradlew bootRun                  # Run the application
./gradlew clean build              # Clean and rebuild
./gradlew test                     # Run all tests
./gradlew test --tests ClassName   # Run specific test class
./gradlew test --tests ClassName.methodName  # Run specific test method
./gradlew bootJar                  # Create executable JAR
./gradlew tasks                    # List all available Gradle tasks
./gradlew dependencies             # Show dependency tree
```

## Code Style Guidelines

### Formatting
- Use 4 spaces for indentation (tabs not allowed)
- No trailing whitespace
- 1 blank line between methods
- Package declaration at top, followed by imports, then class

### Import Organization
- Group 1: Standard Java imports (java.*)
- Group 2: Jakarta imports (jakarta.*)
- Group 3: Third-party libraries (org.springframework.*, lombok.*, etc.)
- Group 4: Project imports (cl.pablovillarroel.*)
- Alphabetically sort within each group
- No wildcard imports (*)

### Naming Conventions
- Classes: PascalCase (e.g., UserController, UserService)
- Methods: camelCase (e.g., createUser, findByEmail)
- Fields: camelCase (e.g., email, createdAt)
- Database tables: snake_case (e.g., users, created_at)
- Package names: lowercase with dots
- Test methods: descriptive, sentence-like (e.g., shouldReturnUserWhenEmailExists)

### Type Guidelines
- Use Long for ID fields (not long)
- Use Boolean for nullable booleans (not boolean)
- Use Optional<T> for repository methods that may not return results
- Use @NotBlank for String validation
- Use @Size for length constraints

### Lombok Annotations
Use these Lombok annotations consistently:
- @Data for entities and DTOs (generates getters/setters/toString/equals/hashCode)
- @NoArgsConstructor and @AllArgsConstructor for entities/DTOs
- @Builder for flexible object construction
- @RequiredArgsConstructor on service/controller classes (injects final fields)
- @Builder.Default for default field values in builders

### Entity Patterns
```java
@Entity
@Table(name = "table_name")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description")
    @Column(nullable = false, length = 100)
    private String field;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### Service Layer
- Annotate with @Service
- Use @RequiredArgsConstructor for dependency injection (constructor injection)
- Use @Transactional on methods that modify data
- Throw IllegalArgumentException for business validation failures
- Use private methods to map entities to DTOs

### Controller Layer
- Annotate with @RestController
- Use @RequestMapping at class level for base path
- Return ResponseEntity<T> with appropriate HttpStatus
- Use @Valid with @RequestBody for request validation
- Delegate all logic to service layer

### Repository Layer
- Interfaces extending JpaRepository<Entity, ID>
- Use Optional<T> for methods that may not return results
- Custom queries follow naming convention: findByField, existsByField, etc.

### DTOs
- Use Request and Response suffix pattern (e.g., UserRequest, UserResponse)
- Exclude sensitive data (passwords) from Response DTOs
- Include validation annotations on Request DTOs

### Validation
- Use Jakarta Bean Validation annotations
- Provide meaningful error messages in annotation attributes
- Common annotations: @NotBlank, @Email, @Size, @NotNull

### Testing
- Use JUnit 5 (Jupiter)
- Use @SpringBootTest for integration tests
- Use @WebMvcTest for controller tests
- Use @DataJpaTest for repository tests
- Use @WithMockUser for security tests

### Database Migrations
- Located in src/main/resources/db/migration/
- Naming: V{version}__{description}.sql (e.g., V2__add_time_entries_table.sql)
- Use snake_case for table and column names
- Create indexes for frequently queried columns
- Add comments for clarity

### Error Handling
- Use IllegalArgumentException for business rule violations
- Throw exceptions with descriptive messages
- Controllers can return appropriate HTTP status codes

## Project Structure

```
cl.pablovillarroel.timetracker/
├── controller/    # REST controllers
├── service/       # Business logic
├── repository/    # JPA repositories
├── model/         # JPA entities
├── dto/           # Data transfer objects
└── config/        # Configuration classes
```

## Dependencies
- Spring Boot 4.0.1
- Java 21
- PostgreSQL with Flyway
- Spring Security
- Spring Data JPA
- Lombok
- Jakarta Validation
