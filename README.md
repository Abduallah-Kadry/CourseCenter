# CourseCenter 🎓

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-ready-blue?style=for-the-badge&logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

A comprehensive Spring Boot application for managing courses with full CRUD operations, JWT authentication, and Swagger API documentation.

[Features](#-features) • [Installation](#-installation) • [API Docs](#-api-documentation) • [Contributing](#-contributing)

</div>

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
  - [Running with Docker](#-running-with-docker)
  - [Running Locally](#-running-locally)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Authentication & Authorization](#-authentication--authorization)
- [Data Flow](#-data-flow)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

## 🚀 Features

### Core Functionality
- ✅ **Complete CRUD Operations**: Full Create, Read, Update, Delete functionality for all 5 standard operations
- 🔐 **JWT Authentication**: Secure user authentication and authorization with password encryption
- 📚 **RESTful API**: Well-structured REST endpoints with standardized API response entities
- 📖 **Swagger Documentation**: Interactive API documentation for easy testing and exploration
- 📄 **Pagination Support**: Efficient data retrieval with built-in pagination
- 🔍 **Data Filtering**: Search and filter capabilities for better data management

### Technical Features
- 🏗️ **Clean Architecture**: DTO pattern implementation
  - **Inbound**: Controller → Request DTOs (with authentication/JWT validation)
  - **Outbound**: Database Entity → Response DTOs
- 🐳 **Docker Support**: Containerized deployment with Docker Compose
- 💾 **Database Management**: Auto-update schema using Hibernate DDL mode `update`
- 🔄 **Data Persistence**: Shared data configuration between environments
- 🔒 **Security**: Full user management with encrypted passwords
- 📊 **Logging**: Comprehensive application logging
- ✔️ **Validation**: Input validation at controller level

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Security**: Spring Security with JWT
- **Validation**: Hibernate Validator
- **ORM**: Spring Data JPA / Hibernate

### Database
- **Database**: MySQL 8.0
- **Connection Pool**: HikariCP
- **Schema Management**: Hibernate DDL Auto-Update

### API & Documentation
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **API Design**: RESTful principles
- **Response Format**: Standardized API Response Entity

### DevOps & Tools
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven 3.8+
- **Version Control**: Git

## 🏛️ Architecture

### Data Flow Pattern

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENT REQUEST                        │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│               CONTROLLER LAYER                           │
│  • Receives Request DTOs                                 │
│  • JWT Authentication & Validation                       │
│  • Route Handling                                        │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                SERVICE LAYER                             │
│  • Business Logic                                        │
│  • Authorization Checks                                  │
│  • Data Transformation                                   │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│              REPOSITORY LAYER                            │
│  • JPA Repository                                        │
│  • Database Operations                                   │
│  • Entity Management                                     │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                 MySQL DATABASE                           │
│  • Data Persistence                                      │
│  • Entity Storage                                        │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│              RESPONSE GENERATION                         │
│  Entity → DTO → Response Entity → JSON                   │
└─────────────────────────────────────────────────────────┘
```

### DTO Pattern Implementation

**Inbound Flow (Request):**
```
User Request → Controller (JWT Auth) → Request DTO → Service Layer
```

**Outbound Flow (Response):**
```
Database Entity → Response DTO → API Response Entity → JSON
```

## 📋 Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| Java JDK | 17+ | Runtime environment |
| Docker | 20.x+ | Containerization |
| Docker Compose | 2.x+ | Multi-container orchestration |
| Maven | 3.8+ | Build tool (optional) |
| MySQL | 8.0+ | Database (if running without Docker) |
| Git | 2.x+ | Version control |

## 💻 Installation

### 🐳 Running with Docker

**Step-by-step Docker Setup:**

```bash
# 1. Clone the repository
git clone https://github.com/Abduallah-Kadry/CourseCenter.git
cd CourseCenter


# 2. Build and start containers
docker-compose up --build -d

# 3. Verify containers are running
docker-compose ps

# 4. View application logs
docker-compose logs -f app

# 6. Access the application
# Application: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

#### Docker Services

| Service | Container | Port | Description |
|---------|-----------|------|-------------|
| **app** | coursecenter-app | 8080 | Spring Boot application |
| **mysql** | coursecenter-mysql | 3306 | MySQL database server |

#### Useful Docker Commands

```bash
# Stop all services
docker-compose down

# Stop and remove all data (volumes)
docker-compose down -v

# Rebuild specific service
docker-compose up --build app

# Access application container shell
docker-compose exec app bash

# Access MySQL container
docker-compose exec mysql mysql -u root -p

# View logs for specific service
docker-compose logs -f mysql

# Restart services
docker-compose restart

# Check container resource usage
docker stats
```

### 🔧 Running Locally (Without Docker)

```bash
# 1. Clone the repository
git clone https://github.com/Abduallah-Kadry/CourseCenter.git
cd CourseCenter

# 2. Create MySQL database
mysql -u root -p
CREATE DATABASE coursecenter;
EXIT;

# 3. Update application.properties with your database credentials

# 4. Build the project
mvn clean install
# or
./gradlew build

# 5. Run the application
mvn spring-boot:run
# or
./gradlew bootRun
# or
java -jar target/coursecenter-0.0.1-SNAPSHOT.jar

# 6. Access the application
# Application: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

## 🔧 Configuration

### Docker Compose Configuration

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: coursecenter-mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD:-rootpassword}
      MYSQL_DATABASE: ${DB_NAME:-coursecenter}
      MYSQL_USER: ${DB_USERNAME:-appuser}
      MYSQL_PASSWORD: ${DB_PASSWORD:-apppassword}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - coursecenter-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    container_name: coursecenter-app
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/${DB_NAME:-coursecenter}
      - SPRING_DATASOURCE_USERNAME=${DB_USERNAME:-appuser}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD:-apppassword}
      - JWT_SECRET=${JWT_SECRET:-your-secret-key-change-in-production}
    ports:
      - "8080:8080"
    networks:
      - coursecenter-network
    volumes:
      - app_logs:/logs

volumes:
  mysql_data:
  app_logs:

networks:
  coursecenter-network:
    driver: bridge
```

### Application Properties

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/coursecenter?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
app.jwt.secret=yourSecretKeyForJWTTokenGeneration
app.jwt.expiration=86400000
app.jwt.refresh-expiration=604800000

# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method

# Logging Configuration
logging.level.root=INFO
logging.level.com.coursecenter=DEBUG
logging.file.name=logs/application.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

### Environment Variables (.env)

```env
# Database Configuration
DB_NAME=coursecenter
DB_USERNAME=appuser
DB_PASSWORD=secure_password_here

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_min_256_bits
JWT_EXPIRATION=86400000

# Application Configuration
SERVER_PORT=8080
SPRING_PROFILE=prod
```

## 📚 API Documentation

### Accessing Swagger UI

Once the application is running, access the interactive API documentation:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`


### Standard CRUD Operations

Each entity supports the following 5 standard CRUD operations:

| Operation | HTTP Method | Endpoint | Description |
|-----------|-------------|----------|-------------|
| **Create** | POST | `/api/resource` | Create new resource |
| **Read All** | GET | `/api/resource` | Get all resources (with pagination) |
| **Read One** | GET | `/api/resource/{id}` | Get single resource by ID |
| **Update** | PUT | `/api/resource/{id}` | Update existing resource |
| **Delete** | DELETE | `/api/resource/{id}` | Delete resource |

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login user | No |
| POST | `/api/auth/refresh` | Refresh JWT token | Yes |
| GET | `/api/auth/me` | Get current user | Yes |
| POST | `/api/auth/logout` | Logout user | Yes |

### Example API Requests

#### 1. Register User

```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "john.doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "john.doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "createdAt": "2025-11-18T10:30:00Z"
  }
}
```

#### 2. Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "john.doe",
  "password": "SecurePassword123!"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000
  }
}
```

#### 3. Get Resources with Pagination

```bash
GET /api/courses?page=0&size=10&sort=title,asc
Authorization: Bearer <your-jwt-token>
```

**Response:**
```json
{
  "status": "success",
  "message": "Courses retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Introduction to Spring Boot",
        "description": "Learn Spring Boot framework",
        "instructor": "Jane Smith",
        "duration": 40,
        "price": 99.99
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 45,
    "totalPages": 5,
    "first": true,
    "last": false
  }
}
```

#### 4. Create Resource

```bash
POST /api/courses
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Advanced Java Programming",
  "description": "Deep dive into Java",
  "instructor": "John Instructor",
  "duration": 60,
  "price": 149.99
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Course created successfully",
  "data": {
    "id": 46,
    "title": "Advanced Java Programming",
    "description": "Deep dive into Java",
    "instructor": "John Instructor",
    "duration": 60,
    "price": 149.99,
    "createdAt": "2025-11-18T10:30:00Z"
  }
}
```

#### 5. Update Resource

```bash
PUT /api/courses/46
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Advanced Java Programming - Updated",
  "price": 129.99
}
```

#### 6. Delete Resource

```bash
DELETE /api/courses/46
Authorization: Bearer <your-jwt-token>
```

**Response:**
```json
{
  "status": "success",
  "message": "Course deleted successfully"
}
```

### HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request successful, no content to return |
| 400 | Bad Request | Invalid request data or validation error |
| 401 | Unauthorized | Authentication required or token invalid |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists |
| 500 | Internal Server Error | Server error |

## 🔐 Authentication & Authorization

### JWT Token Flow

```
1. User Login
   ↓
2. Credentials Validation
   ↓
3. JWT Token Generation (with expiration)
   ↓
4. Token Sent to Client
   ↓
5. Client Stores Token
   ↓
6. Token Sent in Authorization Header for Each Request
   ↓
7. Server Validates Token
   ↓
8. Request Processed if Valid
```

### Using JWT Tokens

Include the JWT token in the `Authorization` header for protected endpoints:

```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
     http://localhost:8080/api/courses
```

### Security Features

- ✅ **Password Encryption**: BCrypt hashing algorithm
- ✅ **JWT Tokens**: Stateless authentication
- ✅ **Token Expiration**: Configurable token lifetime
- ✅ **Role-Based Access**: User roles for authorization
- ✅ **Protected Endpoints**: Authentication required for sensitive operations
- ✅ **Secure Headers**: Security headers configured
- ✅ **CORS Configuration**: Cross-origin resource sharing setup

### User Roles

```java
public enum Role {
    USER,       // Regular user
    INSTRUCTOR, // Can create/manage courses
    ADMIN       // Full system access
}
```

## 📊 Data Flow

### Request Processing Flow

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Client sends HTTP Request with JWT Token                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. JWT Authentication Filter validates token                │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. Controller receives Request DTO                          │
│    • Validates input data                                   │
│    • Extracts user from security context                    │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. Service Layer processes request                          │
│    • Business logic execution                               │
│    • Authorization checks                                   │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. Repository Layer accesses database                       │
│    • JPA queries executed                                   │
│    • Entity retrieved/saved                                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 6. Entity converted to Response DTO                         │
│    • Sensitive data filtered                                │
│    • Data formatted for API                                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 7. Response Entity wraps DTO                                │
│    • Status, message, data, timestamp added                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 8. JSON response sent to client                             │
└─────────────────────────────────────────────────────────────┘
```



## 🏗️ Project Structure

```
CourseCenter/
├── src/
│   ├── main/
│   │   ├── java/com/coursecenter/
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CourseController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── EnrollmentController.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── CourseService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── EnrollmentService.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   └── EnrollmentRepository.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── Enrollment.java
│   │   │   │   └── Role.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── CourseRequest.java
│   │   │   │   │   └── EnrollmentRequest.java
│   │   │   │   │
│   │   │   │   └── response/
│   │   │   │       ├── ApiResponse.java
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── CourseResponse.java
│   │   │   │       └── PageResponse.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── UserDetailsServiceImpl.java
│   │   │   │   └── SecurityConfig.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   ├── WebConfig.java
│   │   │   │   └── JpaConfig.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── DuplicateResourceException.java
│   │   │   │
│   │   │   ├── util/
│   │   │   │   ├── ValidationUtil.java
│   │   │   │   └── ResponseUtil.java
│   │   │   │
│   │   │   └── CourseCenterApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── application-docker.properties
│   │       └── banner.txt
│   │
│   └── test/
│       └── java/com/coursecenter/
│           ├── controller/
│           │   ├── AuthControllerTest.java
│           │   └── CourseControllerTest.java
│           ├── service/
│           │   ├── AuthServiceTest.java
│           │   └── CourseServiceTest.java
│           └── repository/
│               └── UserRepositoryTest.java
│
├── docker/
│   ├── Dockerfile
│   └── docker-compose.yml
│
├── .env.example
├── .gitignore
├── .dockerignore
├── pom.xml
├── README.md
└── LICENSE
```

