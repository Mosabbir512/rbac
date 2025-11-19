# RBAC Spring Boot Implementation

A complete Role-Based Access Control system built with Spring Boot, Spring Security, and JWT authentication.

## 🚀 Features
- User Registration & Login with JWT
- Role-Based Access Control (USER & ADMIN)
- Product CRUD Operations
- MySQL Database
- Spring Security 6

## 🛠 Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Security 6
- JWT Authentication
- MySQL
- Maven

## 📚 API Documentation

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login

### Products (RBAC Protected)
- `GET /api/products` - Get all products (PRODUCT_READ)
- `GET /api/products/{id}` - Get product by ID (PRODUCT_READ)
- `POST /api/products` - Create product (PRODUCT_WRITE - Admin only)
- `PUT /api/products/{id}` - Update product (PRODUCT_UPDATE - Admin only)
- `DELETE /api/products/{id}` - Delete product (PRODUCT_DELETE - Admin only)

### Admin Endpoints
- `GET /api/admin/users` - Get all users (Admin only)
- `POST /api/admin/promote/{username}` - Promote user to admin (Admin only)

## 👤 Default Users
- **Admin:** username: `admin`, password: `admin123`
- **User:** username: `user`, password: `user123`

## 🚀 Quick Start

### Prerequisites
- Java 17
- MySQL 8.0+
- Maven 3.6+

### Installation
1. Clone the repository
   ```bash
   git clone https://github.com/Mosabbir512/rbac.git
   cd rbac
