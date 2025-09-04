---
Last-Modified: 2025-09-05 00:19
---
# 📘 Project Best Practices

## 1. Project Purpose
CourseCenter is a Spring Boot (Java 17) application intended to serve as a backend service for course management. It currently exposes a simple REST endpoint and includes dependencies for security, data persistence (JPA), monitoring (Actuator), and a MySQL driver, indicating a roadmap toward a secure, data-backed API.

## 2. Project Structure
- High-level layout
  - pom.xml — Maven build and dependency management
  - src/main/java — Application source code
    - com.app.coursecenter.CourseCenterApplication — Application entry point (@SpringBootApplication)
    - com.app.coursecenter.controller.coursecontroller.CourseController — Example REST controller with GET /hello
  - src/main/resources
    - application.properties — Application configuration
  - src/test/java — Test sources (JUnit 5, Spring Boot Test)
  - target/ — Build output (ignored by VCS)

- Recommended layering (as the project grows)
  - controller (web layer) — REST controllers and request/response mapping
  - service (domain/application layer) — Business logic and orchestration
  - repository (data access layer) — Spring Data JPA repositories
  - model/domain — Entities and domain models
  - dto — Request/response data transfer objects
  - config — Configuration classes (security, CORS, Jackson, etc.)
  - exception — Custom exceptions and global exception handlers
  - util — Cross-cutting utilities

- Package naming
  - Use lowercase package names (e.g., com.app.coursecenter.controller). Avoid capitalized package segments (e.g., "Controller").

## 3. Test Strategy
- Frameworks
  - JUnit 5 via spring-boot-starter-test
  - spring-security-test for secured endpoints and authentication/authorization testing

- Organization & naming
  - Mirror main package structure under src/test/java
  - ClassNameTests for test classes (e.g., CourseControllerTests)
  - Separate unit vs integration tests by naming or Maven profile (e.g., *IT for integration)

- Test types
  - Unit tests
    - Use pure JUnit/Mockito where possible
    - Controllers: prefer @WebMvcTest with MockMvc to slice the web layer
    - Services: use Mockito for collaborators; avoid Spring context where not needed
  - Integration tests
    - Use @SpringBootTest for end-to-end context
    - Data layer: use @DataJpaTest for repository tests
    - Consider Testcontainers for MySQL-backed integration tests; avoid relying on a developer’s local DB

- Mocking & data setup
  - Use @MockBean to provide mocks in slice tests
  - Use builders/factories or test fixtures for consistent test data

- Coverage & quality
  - Target meaningful coverage on services and domain logic (>80% where practical)
  - Add JaCoCo Maven plugin for coverage reporting in CI

## 4. Code Style
- Language and versions
  - Java 17 (configured via pom.xml)

- Naming conventions
  - Classes & interfaces: PascalCase (e.g., CourseService)
  - Methods & variables: camelCase
  - Constants: UPPER_SNAKE_CASE
  - Packages: lowercase only
  - Controllers named with suffix Controller (e.g., HelloController), endpoints grouped by resource

- REST API design
  - Use resource-based URIs and versioning (e.g., /api/v1/courses)
  - Prefer DTOs for input/output; avoid exposing entities directly
  - Validate inputs using Jakarta Validation annotations (@NotNull, @Size, etc.) and @Valid

- Error handling
  - Implement a centralized @ControllerAdvice with @ExceptionHandler methods
  - Return structured error payloads with consistent fields (timestamp, path, message, code)

- Immutability & Lombok
  - Lombok is available; use judiciously (@Getter, @Setter, @Builder) while keeping clarity
  - Prefer immutable DTOs (constructors, builders) where possible

- Null & Optional
  - Avoid returning null; prefer Optional for absent values in service/repository methods

- Logging
  - Use slf4j (via Lombok’s @Slf4j or LoggerFactory)
  - Log at appropriate levels (INFO for lifecycle events, WARN/ERROR for problems)
  - Avoid logging sensitive data (credentials, tokens, PII)

## 5. Common Patterns
- Layered architecture: controller → service → repository
- Spring Data JPA repositories for persistence
- DTO mapping (consider MapStruct for compile-time mapping as the project grows)
- Validation at the edge (controller level) with @Valid and constraint annotations
- Global exception handling with @ControllerAdvice
- Security configuration via a SecurityFilterChain bean (Spring Security 6+)

## 6. Do's and Don'ts
- Do
  - Use lowercase packages; avoid capitalized package names
  - Introduce clear layers (controller/service/repository) as features grow
  - Externalize secrets and environment-specific properties
  - Secure Actuator endpoints; expose only necessary ones
  - Add API versioning and request validation early
  - Write focused unit tests and targeted slice tests (@WebMvcTest, @DataJpaTest)
  - Use Testcontainers for DB-dependent tests
  - Add database migrations with Flyway or Liquibase

- Don't
  - Don’t hardcode credentials in properties files
  - Don’t use generic class names like Controller; make names descriptive (e.g., HelloController)
  - Don’t expose entities directly in controller responses
  - Don’t rely on a local DB for integration tests; prefer containers
  - Don’t log secrets or PII

## 7. Tools & Dependencies
- Core dependencies (from pom.xml)
  - spring-boot-starter-actuator — Health and metrics endpoints
  - spring-boot-starter-data-jpa — JPA and Hibernate integration
  - spring-boot-starter-security — Authentication and authorization
  - spring-boot-devtools (runtime) — Hot reload for local development
  - mysql-connector-j (runtime) — MySQL JDBC driver
  - lombok — Boilerplate reduction (optional)
  - spring-boot-starter-test — JUnit 5 and testing support
  - spring-security-test — Security testing utilities

- Build & run
  - Java 17 required
  - Build: mvn clean package
  - Run: mvn spring-boot:run or java -jar target/CourseCenter-0.0.1-SNAPSHOT.jar

- Configuration
  - Prefer application.yml over application.properties for structure, but both are fine
  - Use profiles (application-local.yml, application-dev.yml, application-prod.yml)
  - Activate with SPRING_PROFILES_ACTIVE=local/dev/prod
  - Example DB settings (use env or profile-specific files):
    - spring.datasource.url
    - spring.datasource.username
    - spring.datasource.password
    - spring.jpa.hibernate.ddl-auto (validate/update)

- Security notes
  - Replace default in-memory user configuration with a SecurityFilterChain bean and a UserDetailsService/DAO
  - Use a PasswordEncoder (e.g., BCryptPasswordEncoder)
  - Configure CORS for API clients if needed
  - Restrict Actuator endpoints and avoid exposing sensitive info

## 8. Other Notes
- Current state
  - A sample GET /hello endpoint exists
  - application.properties contains a hardcoded Spring Security user and password; move credentials to environment variables or profile-specific secure storage immediately

- For LLM-generated code in this repo
  - Maintain lowercase package names and descriptive class names
  - Add or modify code following the layered architecture
  - Add tests alongside new features (unit and slice tests preferred)
  - When introducing persistence, create entities, repositories, and migration scripts
  - Provide or update SecurityFilterChain when adding secured endpoints
  - Use DTOs and validation for all request bodies
