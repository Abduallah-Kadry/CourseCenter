# CourseCenter 🎓

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-ready-blue?style=for-the-badge&logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

A comprehensive Spring Boot application for managing courses with full CRUD operations, JWT authentication, and Swagger API documentation.

[Features](#-features) • [Installation](#-installation) • [API Docs](#-api-documentation)

</div>

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
  - [Running with Docker](#-running-with-docker)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Data Flow](#-data-flow)

## 🚀 Features

### Core Functionality
- ✅ **Complete CRUD Operations**: Full Create, Read, Update, Delete functionality for all 5 standard operations
- 🔐 **JWT Authentication**: Secure user authentication and authorization with password encryption
- 📚 **RESTful API**: Well-structured REST endpoints with standardized API response entities
- 📖 **Swagger Documentation**: Interactive API documentation for easy testing and exploration
- 📄 **Pagination Support**: Efficient data retrieval with built-in pagination

### Technical Features
- 🏗️ **Clean Architecture**: DTO pattern implementation
  - **Inbound**: Controller → Request DTOs (with authentication/JWT validation)
  - **Outbound**: Database Entity → Response DTOs
- 🐳 **Docker Support**: Containerized deployment with Docker Compose
- 💾 **Database Management**: Auto-update schema using Hibernate DDL mode `update`
- 🔄 **Data Persistence**: Shared data configuration between environments
- 🔒 **Security**: Full user management with encrypted passwords
- ✔️ **Validation**: Input validation at controller level
- 🔔 **Event-Driven Architecture**: Apache Kafka integration for asynchronous messaging
- 📧 **Notification Service**: Real-time notifications via Kafka consumers

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

### Messaging & Events
- **Message Broker**: Apache Kafka
- **Use Cases**:
  - Course reservation events
  - Notification service delivery
  - Asynchronous event processing

### API & Documentation
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **API Design**: RESTful principles
- **Response Format**: Standardized API Response Entity

### DevOps & Tools
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven 3.8+
- **Version Control**: Git


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

# 3. Build and start containers
docker-compose up --build -d

# 4. View application logs
docker-compose logs -f app

# 5. Access the application
# Application: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

#### Docker Services

| Service | Container | Port | Description |
|---------|-----------|------|-------------|
| **app** | coursecenter-app | 8080 | Spring Boot application |
| **mysql** | coursecenter-mysql | 3306 | MySQL database server |
| **kafka** | coursecenter-kafka | 9092 | Apache Kafka message broker |
| **zookeeper** | coursecenter-zookeeper | 2181 | Kafka coordination service |



## 📚 API Documentation

### Accessing Swagger UI

Once the application is running, access the interactive API documentation:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs (JSON)**: `http://localhost:8080/api-docs`
- **API Docs (YAML)**: `http://localhost:8080/api-docs.yaml`

### API Response Entity Structure

All API responses follow a standardized format with consistent structure for success and error cases.

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


### Data Sharing Configuration

- **DDL Mode**: `update` - Automatically updates schema without data loss
- **Data Sharing**: Entity data is shared via GitHub repository
- **Data Uniqueness**: Each environment maintains unique data instances
- **Schema Updates**: Hibernate automatically handles schema changes


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
│    • Publishes Kafka events (for enrollments)              │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. Repository Layer accesses database                       │
│    • JPA queries executed                                   │
│    • Entity retrieved/saved                                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 6. Kafka Event Publishing (Async)                          │
│    • Course reservation event → course-reservation-topic    │
│    • Notification event → notification-topic                │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 7. Entity converted to Response DTO                         │
│    • Sensitive data filtered                                │
│    • Data formatted for API                                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 8. Response Entity wraps DTO                                │
│    • Status, message, data, timestamp added                 │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 9. JSON response sent to client                             │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 10. Kafka Consumers Process Events (Async)                 │
│     • Notification Service sends emails/SMS                 │
│     • Analytics Service updates metrics                     │
│     • Audit Service logs events                             │
└─────────────────────────────────────────────────────────────┘
```

## 💾 Database Schema
```
┌─────────────────────┐         ┌──────────────────────┐         ┌─────────────────┐
│        USER         │         │  COURSE_RESERVATION  │         │     COURSE      │
├─────────────────────┤         ├──────────────────────┤         ├─────────────────┤
│ id (PK)             │◄────────┤ id (PK)              │         │ id (PK)         │
│ username (UQ)       │         │ user_id (FK)         │         │ title           │
│ email (UQ)          │         │ course_id (FK)       ├────────►│ description     │
│ password            │         │ course_rate          │         │ instructor      │
│ first_name          │         │ rating_time          │         │ duration        │
│ last_name           │         │ reservation_time     │         │ price           │
│ role                │         │                      │         │ created_at      │
│ created_at          │         │ UNIQUE(user_id,      │         │ updated_at      │
│ updated_at          │         │        course_id)    │         │                 │
└─────────────────────┘         └──────────────────────┘         └─────────────────┘
```

## 🏗️ Project Structure

```
└───com
    └───app
        └───coursecenter
            ├───Annotaions
            ├───audit
            ├───config
            │   ├───securityconfig
            │   │   └───kafka
            │   └───webconfig
            ├───controller
            │   ├───coursecontroller
            │   ├───frontend
            │   └───usercontroller
            ├───dto
            ├───entity
            ├───exception
            │   └───ExceptionsAdvice
            ├───mapper
            ├───repository
            ├───request
            ├───response
            ├───service
            │   ├───adminservice
            │   ├───authenticationservice
            │   ├───courseservice
            │   ├───FileStorageService
            │   └───userservice
            ├───util
            └───Validators

```

## 🔔 Kafka Integration

### Event-Driven Architecture

The CourseCenter application uses Apache Kafka for asynchronous event processing and inter-service communication. This enables scalable, decoupled microservices architecture.

### Course Reservation Events

When users interact with course enrollments, events are published to Kafka for asynchronous processing:

**Producer Flow**:
```
User Enrollment → Service Layer → KafkaProducer → course-reservation-topic
```

**Event Types**:
- `COURSE_RESERVED`: Triggered when user successfully enrolls in a course
- `COURSE_CANCELLED`: Triggered when user cancels enrollment

### Notification Service

The notification service consumes events from Kafka and handles multi-channel notifications:

**Consumer Flow**:
```
Kafka Topics → NotificationConsumer → NotificationService → External Channels
```

**Supported Channels**:
- **Email**: Confirmation emails, course updates, reminders

**Notification Topics**:
- `notification-topic`: Main notification queue


### Kafka Topics Configuration

| Topic Name | Purpose | Partitions | Replication |
|------------|---------|------------|-------------|
| `course-reservation-topic` | Course enrollment events | 3 | 1 |
| `notification-topic` | User notifications | 3 | 1 |


### Monitoring Kafka

```bash
# Check Kafka topics
docker-compose exec kafka kafka-topics --list --bootstrap-server localhost:9092

# Describe a topic
docker-compose exec kafka kafka-topics --describe \
  --topic course-reservation-topic \
  --bootstrap-server localhost:9092

# View consumer groups
docker-compose exec kafka kafka-consumer-groups --list \
  --bootstrap-server localhost:9092

# Check consumer lag
docker-compose exec kafka kafka-consumer-groups --describe \
  --group notification-service-group \
  --bootstrap-server localhost:9092
```