# 🚀 Dynamic_FT Project Overview

## 🎯 Vision

Build a practice monorepo where you create many tiny, runnable services (and UIs) to become **interview-ready** across backend, frontend, databases, DevOps, testing, and microservices.

Each slice: small feature, runs locally, has clear endpoints and a short doc (WHAT/WHY/HOW). Later you add DB, tests, CI/CD, and deploy to AWS.

---

## 📁 Repository Structure & Conventions

### **Repository**: `dynamic_FT`

```
dynamic_FT/
├── dynamic_FE/                    # Frontend services
├── dynamic_BE/                    # Backend services  
├── topics/                        # Learning topics and references
├── scripts/                       # Automation scripts
├── docs/                          # Project documentation
├── compose.yaml                   # Docker Compose for local development
└── .github/workflows/             # CI/CD pipelines
```

### **Naming Convention**
```
<framework>_<language>_t<topicId>-<slug>_s<ordinal>
```

**Examples:**
- `springboot_java_t01-orders_s1` - Spring Boot Orders Service #1
- `react_js_t01-orders_s1` - React Orders UI #1
- `express_js_t02-products_s1` - Express Products Service #1

### **Port Assignment (Deterministic)**

#### **Frontend Ports**: `4000 + topicId*10 + feOrdinal`
- React Orders UI (t01, s1): `4000 + 1*10 + 1 = 4011`
- Angular Products UI (t02, s1): `4000 + 2*10 + 1 = 4021`

#### **Backend Ports**: `7000 + topicId*10 + beOrdinal`  
- Spring Boot Orders API (t01, s1): `7000 + 1*10 + 1 = 7011`
- Express Products API (t02, s1): `7000 + 2*10 + 1 = 7021`
- Django Customers API (t03, s1): `7000 + 3*10 + 1 = 7031`
- Flask Reviews API (t04, s1): `7000 + 4*10 + 1 = 7041`

---

## 🎓 Skills Focus Areas

### **Backend Technologies**
- **Spring Boot** (Java) - Enterprise Java development
- **Express.js** (Node.js) - JavaScript backend services
- **Django** (Python) - Full-featured Python framework
- **Flask** (Python) - Lightweight Python microservices

### **Frontend Technologies**
- **React** (JavaScript) - Component-based UI library
- **Angular** (TypeScript) - Full-featured framework
- **Micro-frontends** - Independent deployable SPAs

### **Database Technologies**
- **SQL**: MySQL, PostgreSQL, Oracle
- **NoSQL**: MongoDB, DynamoDB
- **Caching**: Redis

### **Cloud & DevOps**
- **AWS Services**: ECS, ECR, EC2, RDS, DynamoDB, CloudFront, CloudWatch
- **Containers**: Docker, Kubernetes (optional)
- **Infrastructure**: Terraform
- **CI/CD**: Jenkins, GitHub Actions
- **Build Tools**: Maven, Gradle, npm

### **Testing Stack**
- **Backend**: JUnit, Mockito, Karate, Cucumber, Pytest
- **Frontend**: Jasmine, Cypress, Playwright, Selenium
- **API Testing**: Postman, REST Assured

### **Other Technologies**
- **Version Control**: Git
- **API Design**: REST, GraphQL
- **Message Queues**: Apache Kafka
- **Monitoring**: Application and infrastructure monitoring

---

## 🏗️ Initial Service Architecture (6 Core Slices)

### **Backend Services**

| Service | Technology | Port | Purpose | Data Store |
|---------|------------|------|---------|------------|
| **Orders API** | Spring Boot + Java | 7011 | CRUD operations for orders | MySQL |
| **Products API** | Express.js + Node.js | 7021 | Product catalog management | DynamoDB |
| **Customers API** | Django + Python | 7031 | Customer management | PostgreSQL |
| **Reviews API** | Flask + Python | 7041 | Product reviews and ratings | MongoDB |

### **Frontend Services**

| Service | Technology | Port | Purpose | Connects To |
|---------|------------|------|---------|-------------|
| **Orders UI** | React + JavaScript | 4011 | Order management interface | Orders API (7011) |
| **Products UI** | Angular + TypeScript | 4021 | Product browsing interface | Products API (7021) |

### **Infrastructure Services**
- **Docker Compose** - Local development environment
- **CI/CD Skeleton** - GitHub Actions workflows
- **Documentation** - Comprehensive project docs

---

## 🗄️ Data Store Strategy

### **Realistic Database Mapping**

```
🔄 Orders Service
├── Spring Boot + Java 21
├── Local: MySQL (Docker, port 3307→3306)
└── Production: AWS RDS MySQL

➕ Products Service  
├── Express.js + Node.js
├── Local: DynamoDB Local (port 8000)
└── Production: AWS DynamoDB

👥 Customers Service
├── Django + Python
├── Local: PostgreSQL (Docker, port 5433→5432)  
└── Production: AWS RDS PostgreSQL

⭐ Reviews Service
├── Flask + Python
├── Local: MongoDB (Docker, port 27018→27017)
└── Production: MongoDB Atlas / AWS DocumentDB
```

### **Evolution Strategy**
1. **Phase 1**: In-memory storage (rapid prototyping)
2. **Phase 2**: Local databases (Docker Compose)
3. **Phase 3**: Cloud databases (AWS managed services)

---

## 🌍 Environment Strategy

### **Local Development**

#### **Database Setup (Docker Compose)**
```yaml
services:
  mysql:
    ports: ["3307:3306"]  # Orders database
  postgres: 
    ports: ["5433:5432"]  # Customers database
  dynamodb-local:
    ports: ["8000:8000"]  # Products database
  mongodb:
    ports: ["27018:27017"] # Reviews database
```

#### **Service Configuration**
- Each service uses `.env.local` or `application-local.properties`
- CORS configured for `http://localhost:4011`, `http://localhost:4021`
- Services can run individually (IDE debug) or together (Docker Compose)

### **Production (AWS)**

#### **Backend Services**
```
Docker Images → Amazon ECR → ECS Fargate
                      ↓
            Application Load Balancer
                (Path-based routing)
                      ↓
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
   Orders API    Products API   Customers API
   (ECS Task)    (ECS Task)     (ECS Task)
```

#### **Database Services**
- **Orders**: RDS MySQL (managed)
- **Products**: DynamoDB (serverless)
- **Customers**: RDS PostgreSQL (managed)  
- **Reviews**: MongoDB Atlas or DocumentDB

#### **Frontend Services**
```
React/Angular Build → S3 Bucket → CloudFront CDN
                           ↓
                   Runtime config.json
                   (API base URLs)
```

#### **Observability**
- **Logs**: ECS → CloudWatch Logs
- **Metrics**: CloudWatch Metrics
- **Access Logs**: ALB → S3
- **Secrets**: AWS Systems Manager / Secrets Manager

---

## 🧩 Microservices Communication

### **Current Architecture (Phase 1)**
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│ React Orders UI │───▶│ Spring Boot API  │───▶│ In-Memory       │
│ (port 4011)     │    │ (port 7011)      │    │ ArrayList       │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### **Target Architecture (Phase 3)**
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│ React Orders UI │───▶│ Spring Boot API  │───▶│ MySQL (Orders)  │
│ (port 4011)     │    │ (port 7011)      │    │ RDS             │
└─────────────────┘    └──────────────────┘    └─────────────────┘

┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│Angular Products │───▶│ Express.js API   │───▶│ DynamoDB        │
│ (port 4021)     │    │ (port 7021)      │    │ (Products)      │
└─────────────────┘    └──────────────────┘    └─────────────────┘

┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│ Admin Portal    │───▶│ Django API       │───▶│ PostgreSQL      │
│ (port 4031)     │    │ (port 7031)      │    │ (Customers)     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

---

## 📋 Current Implementation Status

### **✅ Completed**

#### **Orders Service** (`springboot_java_t01-orders_s1`)
- **Status**: ✅ **Phase 1 Complete** (In-Memory)
- **Location**: `dynamic_BE/springboot_java_t01-orders_s1/`
- **Technology**: Spring Boot 3.3.3 + Java 21
- **Port**: 7011
- **Features**:
  - ✅ Complete CRUD API
  - ✅ DTO/Entity mapping architecture 
  - ✅ Comprehensive documentation
  - ✅ Postman collection ready
  - ✅ Health endpoints
  - ✅ Spring Boot Actuator

#### **API Endpoints**
| Method | Endpoint | Purpose |
|--------|----------|---------|
| `GET` | `/api/v1/orders/health` | Health check |
| `GET` | `/api/v1/orders` | List all orders |
| `GET` | `/api/v1/orders/{id}` | Get order by ID |
| `POST` | `/api/v1/orders` | Create new order |
| `PUT` | `/api/v1/orders/{id}` | Update order |
| `DELETE` | `/api/v1/orders/{id}` | Delete order |

#### **Data Models**
```java
// API Layer (DTO)
OrderDto(id, item, price, status)

// Business Layer (Entity) 
Order(id, item, price, status, createdAt, updatedAt, customerId, internalNotes)

// Order Status Lifecycle
PENDING → CONFIRMED → SHIPPED → DELIVERED
    ↓
CANCELLED (from any state)
```

### **🔜 Next Planned**

#### **Products Service** (`express_js_t02-products_s1`)
- **Technology**: Express.js + Node.js
- **Port**: 7021
- **Database**: DynamoDB Local → DynamoDB

#### **React Orders UI** (`react_js_t01-orders_s1`)
- **Technology**: React + JavaScript
- **Port**: 4011
- **Connects To**: Orders API (7011)

---

## 🎯 Orders Service Evolution Roadmap

### **Phase 1: ✅ In-Memory (Current)**
```java
OrderService → ArrayList<OrderDto> → Port 7011
```
- ✅ Complete CRUD operations
- ✅ REST API with validation
- ✅ DTO/Entity separation
- ✅ Health monitoring

### **Phase 2: 🔜 Database Integration (Next)**
```java
OrderService → OrderMapper → Order Entity → MySQL
```
**Implementation Steps:**
1. Add JPA/Hibernate dependencies to `build.gradle`
2. Uncomment JPA annotations in `Order.java` entity
3. Add MySQL configuration to `application.properties`
4. Create `OrderRepository` interface
5. Update `OrderService` to use repository instead of ArrayList
6. Add Docker Compose with MySQL

### **Phase 3: 🎯 Production Ready (Future)**
```
Docker → ECR → ECS Fargate → ALB → RDS MySQL
```
**Implementation Steps:**
1. Add comprehensive error handling
2. Add validation and business rules
3. Add logging and monitoring
4. Create Docker image
5. Add CI/CD pipeline
6. Deploy to AWS ECS
7. Configure RDS MySQL
8. Set up monitoring and alerting

---

## 🏆 Learning Outcomes

### **Technical Skills Developed**
- **Backend Development**: REST API design, Spring Boot ecosystem
- **Database Design**: Entity modeling, JPA/ORM patterns
- **Architecture Patterns**: DTO/Entity mapping, layered architecture
- **API Documentation**: OpenAPI/Swagger, comprehensive README
- **Testing**: Manual testing, Postman collections, automated testing
- **DevOps**: Docker containerization, cloud deployment
- **Monitoring**: Health checks, application metrics

### **Interview Readiness**
- **System Design**: Microservices architecture understanding
- **Scalability**: Database selection and optimization
- **Best Practices**: Clean code, separation of concerns
- **Real-world Experience**: End-to-end application development
- **Problem Solving**: Debug and troubleshoot distributed systems

---

## 🚀 Getting Started

### **Prerequisites**
- Java 21+
- Gradle 9.1.0+
- Docker (for databases)
- Postman (for API testing)

### **Quick Start Orders Service**
```bash
# Navigate to orders service
cd dynamic_FT/dynamic_BE/springboot_java_t01-orders_s1

# Build and run
gradle clean build
gradle bootRun

# Test health endpoint
curl http://localhost:7011/api/v1/orders/health
```

### **Next Steps**
1. Complete Orders Service testing with Postman
2. Add MySQL database integration
3. Build React Orders UI
4. Create Products Service with Express.js
5. Set up Docker Compose for local development
6. Deploy to AWS

---

**📝 Document Version**: 1.0  
**📅 Last Updated**: October 2025  
**👥 Project**: Dynamic_FT Interview Preparation Platform  
**🎯 Current Focus**: Orders Service (Spring Boot + Java 21)