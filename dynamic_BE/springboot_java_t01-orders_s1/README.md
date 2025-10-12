# 📋 Orders Service Documentation (springboot_java_t01-orders_s1)

## 🏷️ Service Overview

**Service Name**: `springboot_java_t01-orders_s1`  
**Technology Stack**: Spring Boot 3.3.3 + Java 21  
**Port**: 7011  
**Purpose**: RESTful microservice for managing order operations with CRUD functionality

---

## 🎯 What This Service Does

The Orders Service is a lightweight microservice that provides complete order management capabilities. It allows you to:

- ✅ Create new orders with item name and price
- ✅ Retrieve all orders or specific orders by ID
- ✅ Update existing orders (item, price, status)
- ✅ Delete orders
- ✅ Health check monitoring
- ✅ Status tracking through order lifecycle

---

## 🏗️ Architecture & Layers

### **🌐 API Layer** (`/api` package)
**Location**: `src/main/java/com/dynamic/orders/api/`

#### **OrdersController.java** - REST Controller
- **Purpose**: HTTP endpoint handler
- **Base Path**: `/api/v1/orders`
- **Responsibilities**:
  - HTTP request/response handling
  - Path variable and request body validation
  - Calling service layer methods
  - HTTP status code management

#### **Data Transfer Objects (DTOs)**
- **OrderDto**: Main order representation with id, item, price, status
- **CreateOrderRequest**: Request payload for creating orders
- **UpdateOrderRequest**: Request payload for updating orders
- **OrderStatus**: Enum for order states (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

### **🔧 Service Layer** (`/service` package)
**Location**: `src/main/java/com/dynamic/orders/service/`

#### **OrderService.java** - Business Logic
- **Purpose**: Core business operations
- **Storage**: In-memory ArrayList (non-persistent)
- **Responsibilities**:
  - CRUD operations implementation
  - ID generation using AtomicInteger
  - Business rule enforcement
  - Data manipulation and validation

---

## 🚀 API Endpoints Reference

### **Base URL**: `http://localhost:7011`

| Method | Endpoint | Purpose | Request Body | Response |
|--------|----------|---------|--------------|----------|
| `GET` | `/api/v1/orders/health` | Health check | None | `{"ok": true}` |
| `GET` | `/api/v1/orders` | List all orders | None | OrderDto[] |
| `GET` | `/api/v1/orders/{id}` | Get order by ID | None | OrderDto or 404 |
| `POST` | `/api/v1/orders` | Create new order | CreateOrderRequest | OrderDto (201) |
| `PUT` | `/api/v1/orders/{id}` | Update order | UpdateOrderRequest | OrderDto or 404 |
| `DELETE` | `/api/v1/orders/{id}` | Delete order | None | 204 or 404 |

### **Spring Boot Actuator**
| Method | Endpoint | Purpose |
|--------|----------|---------|
| `GET` | `/actuator/health` | Application health status |

---

## 📊 Data Models

### **OrderDto Structure**
```json
{
  "id": 1,
  "item": "Coffee",
  "price": 4.50,
  "status": "PENDING"
}
```

### **CreateOrderRequest Structure**
```json
{
  "item": "Coffee",     // Required, non-blank
  "price": 4.50         // Required, >= 0
}
```

### **UpdateOrderRequest Structure** (All fields optional)
```json
{
  "item": "Large Coffee",      // Optional
  "price": 5.50,              // Optional, >= 0
  "status": "CONFIRMED"       // Optional, valid enum value
}
```

### **Order Status Lifecycle**
```
PENDING → CONFIRMED → SHIPPED → DELIVERED
    ↓
CANCELLED (from any state)
```

---

## 💾 Current Data Storage

### **⚠️ Important: In-Memory Storage**
- **Type**: ArrayList<OrderDto> in memory
- **Persistence**: **NONE** - all data lost on restart
- **Scope**: Single application instance only
- **ID Generation**: AtomicInteger starting from 1

### **Storage Behavior**
```java
// On app start
orders = []  // Empty list

// After creating orders
orders = [Order{id:1}, Order{id:2}, Order{id:3}]

// On app restart
orders = []  // All data lost!
```

---

## 🔧 Build & Deployment

### **Prerequisites**
- Java 21
- Gradle 9.1.0+

### **Option A: Build with Gradle (Recommended)**
```bash
# Navigate to project directory
cd "C:/Users/canng/dynamic_FT/dynamic_BE/springboot_java_t01-orders_s1"

# Build project
./gradlew build

# Run application
./gradlew bootRun

# On Windows use:
gradlew.bat bootRun
```

### **Option B: Build with Docker**
```bash
# Build Docker image
docker build -t orders-inmem:latest .

# Run container
docker run --rm -p 7011:7011 orders-inmem:latest
```

### **Option C: Setup Gradle via SDKMAN (if needed)**
```bash
# Run setup script (creates Gradle wrapper)
bash run_setup.sh

# Then use the generated wrapper
./gradlew bootRun
```

### **Application Startup**
```bash
# Server starts on port 7011
# Look for: "Started SpringbootJavaT01OrdersS1Application"
# Status: 80% EXECUTING (normal for running service)
```

---

## 🧪 Testing

### **Quick Health Check**
```bash
curl http://localhost:7011/api/v1/orders/health
# Expected: {"ok":true}
```

### **Complete API Testing Workflow**

#### **1. Create Orders**
```bash
# Create first order
curl -X POST http://localhost:7011/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"item": "Coffee", "price": 4.50}'

# Create second order
curl -X POST http://localhost:7011/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"item": "Tea", "price": 3.00}'
```

#### **2. Retrieve Orders**
```bash
# List all orders
curl http://localhost:7011/api/v1/orders

# Get specific order
curl http://localhost:7011/api/v1/orders/1
```

#### **3. Update Order**
```bash
# Update order status and price
curl -X PUT http://localhost:7011/api/v1/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"item": "Large Coffee", "price": 5.50, "status": "CONFIRMED"}'
```

#### **4. Delete Order**
```bash
# Delete order
curl -X DELETE http://localhost:7011/api/v1/orders/1
```

### **Postman Collection**
- Collection name: `SpringbootJavaT01OrdersS1`
- Environment: `Local Development` with `baseUrl=http://localhost:7011`
- Full test suite with automated validations available

---

## 📁 Project Structure

```
springboot_java_t01-orders_s1/
├── src/main/java/com/dynamic/orders/
│   ├── api/                          # REST API Layer
│   │   ├── OrdersController.java     # HTTP endpoints
│   │   ├── OrderDto.java            # Order data model
│   │   ├── CreateOrderRequest.java  # Create request DTO
│   │   ├── UpdateOrderRequest.java  # Update request DTO
│   │   ├── OrderStatus.java         # Status enum
│   │   └── ApiErrorHandler.java     # Error handling
│   ├── service/                     # Business Logic Layer
│   │   └── OrderService.java        # Core business operations
│   ├── config/                      # Configuration
│   │   └── CorsConfig.java          # CORS configuration
│   └── SpringbootJavaT01OrdersS1Application.java  # Main class
├── src/main/resources/
│   └── application.properties       # Configuration
├── build.gradle                     # Build configuration
├── settings.gradle                  # Project settings
├── gradlew, gradlew.bat            # Gradle wrapper
├── Dockerfile                      # Docker build
├── .dockerignore                   # Docker ignore rules
├── run_setup.sh                    # Gradle setup script
└── README.md                       # This documentation
```

---

## ⚙️ Configuration

### **Application Properties** (`application.properties`)
```properties
server.port=7011
management.endpoints.web.exposure.include=health,info
```

### **Key Dependencies** (`build.gradle`)
- `spring-boot-starter-web` - REST API capabilities
- `spring-boot-starter-actuator` - Health monitoring
- `spring-boot-starter-validation` - Request validation

---

## 🚨 Known Limitations

1. **No Data Persistence** - All data lost on restart
2. **Single Instance** - No clustering support
3. **No Authentication** - Open endpoints
4. **No Database** - In-memory storage only
5. **Basic Error Handling** - Default Spring Boot error responses
6. **No Comprehensive Logging** - Default Spring Boot logging only

---

## 🔮 Future Enhancements (Recommendations)

### **High Priority**
- Add database persistence (H2, PostgreSQL, MySQL)
- Implement proper error handling and custom exceptions
- Add comprehensive logging and monitoring
- Add input validation and business rules

### **Medium Priority**
- Add authentication/authorization (JWT, OAuth2)
- Implement pagination for list operations
- Add search and filtering capabilities
- Add audit trail (created/updated timestamps)

### **Low Priority**
- Add Docker containerization improvements
- Implement caching (Redis)
- Add monitoring and metrics (Micrometer)
- Add API documentation (Swagger/OpenAPI)

---

## 🎯 Quick Start Reminder

```bash
# 1. Navigate to project
cd "C:/Users/canng/dynamic_FT/dynamic_BE/springboot_java_t01-orders_s1"

# 2. Start application
./gradlew bootRun

# 3. Test health endpoint
curl http://localhost:7011/api/v1/orders/health

# 4. Create your first order
curl -X POST http://localhost:7011/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"item": "Welcome Back Coffee", "price": 5.00}'

# 5. List all orders
curl http://localhost:7011/api/v1/orders
```

---

## 🛠️ Troubleshooting

### **Common Issues**

#### **Port 7011 already in use**
```bash
# Check what's using the port
netstat -ano | findstr :7011

# Kill the process or change port in application.properties
```

#### **Gradle command not found**
```bash
# Use the wrapper instead
./gradlew bootRun    # Unix/Linux/Mac
gradlew.bat bootRun  # Windows
```

#### **Application won't start**
```bash
# Check Java version
java --version  # Should be Java 21

# Clean and rebuild
./gradlew clean build
```

#### **API returning 404**
- Ensure application started successfully
- Check logs for "Started SpringbootJavaT01OrdersS1Application"
- Verify port 7011 is correct
- Test health endpoint first: `/api/v1/orders/health`

---

**📝 Last Updated**: October 2025  
**🏷️ Service Status**: Development/Learning Project - Not Production Ready  
**👥 Maintainer**: Dynamic FT Team
