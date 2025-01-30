# 🔐 Authentication Service

The **Authentication Service** is a microservice responsible for user authentication and authorization within the system. It provides JWT-based authentication and role-based access control.

---

## 📌 Features

- **User Registration & Login**  
  Secure authentication with JWT tokens.

- **Role-Based Access Control (RBAC)**  
  Supports multiple roles (Admin, Doctor, Patient).

- **Token Management**  
  Generates and validates JWT access and refresh tokens.

- **Password Security**  
  Uses **BCrypt** hashing for password storage.

- **MongoDB Integration**  
  Stores user credentials and roles in MongoDB.

- **Dockerized Deployment**  
  Easily deployable with **Docker Compose**.

---

## 🚀 Technologies Used

### **Backend (Spring Boot + MongoDB)**
- 🟢 **Java 21**
- 🟢 **Spring Boot 3.1.4**
- 🟢 **Spring Security (JWT Authentication)**
- 🟢 **Spring Data MongoDB**
- 🟢 **Lombok**
- 🟢 **ModelMapper**
- 🟢 **Maven**

### **Infrastructure**
- 🟡 **MongoDB** (Stores users and authentication data)
- 🟡 **Docker & Docker Compose**
- 🟡 **Nexus Repository** (for dependency management)
- 🟡 **Jenkins/GitHub Actions** (CI/CD)

---

## 🛠️ Getting Started

### 🔽 Clone the Repository
```sh
git clone https://github.com/your-username/authentication-service.git
cd authentication-service
```

### 🐳 Start Services with Docker Compose
- Ensure Docker and Docker Compose are installed, then run:
```sh
docker compose up -d
```

### 🚀 Run Backend (Spring Boot Application)
```sh
mvn clean package
java -jar target/authentication-service.jar
```
- The backend will be available at http://localhost:8080.