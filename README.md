# 🎓 School API

This is a **Spring Boot REST API** to manage students and courses with authentication and role-based access (Admin & Student). It supports JWT authentication and CRUD operations for courses, as well as enrollment management for students.

---

## 📦 Features

### 🧑‍🎓 Students
- Get own student profile
- View enrolled courses
- Enroll in multiple courses
- Add new courses to current enrollment
- Remove specific courses
- Automatically linked with user registration (as a student)

### 👨‍🏫 Admin
- Register users (as students)
- Enroll/update student courses
- View all students
- Remove courses from any student

### 📚 Courses
- Create new courses
- View all available courses
- Delete existing courses

---

## 🔐 Authentication

- JWT-based login system
- Two roles: `ADMIN` and `STUDENT`
- Default user role during registration: `STUDENT`

---

## 🔗 Endpoints

### 🔐 AuthController
| Method | Endpoint              | Description                    |
|--------|-----------------------|--------------------------------|
| POST   | `/api/auth/register`  | Register new student user      |
| POST   | `/api/auth/login`     | Authenticate and get token     |

---

### 🧑‍🎓 StudentController
| Method | Endpoint                          | Access     | Description                          |
|--------|-----------------------------------|------------|--------------------------------------|
| GET    | `/api/students/me`                | STUDENT    | Get own student info                 |
| GET    | `/api/students/{id}`              | ADMIN      | Get any student by ID                |
| GET    | `/api/students`                   | ADMIN      | Get all students                     |
| POST   | `/api/students/me/courses`        | STUDENT    | Add courses (first-time or update)   |
| DELETE | `/api/students/me/courses/{id}`   | STUDENT    | Remove course from own list          |
| PUT    | `/api/students/{id}/courses`      | ADMIN      | Replace student’s course list        |
| DELETE | `/api/students/{id}/courses/{id}` | ADMIN      | Remove specific course from student  |

---

### 📚 CourseController
| Method | Endpoint              | Access | Description           |
|--------|-----------------------|--------|-----------------------|
| GET    | `/api/courses`        | Any    | View all courses      |
| POST   | `/api/courses`        | ADMIN  | Add new course        |
| DELETE | `/api/courses/{id}`   | ADMIN  | Delete a course       |

---

## 🛠 Technologies

- Java 17
- Spring Boot 3.1.5
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI
- Maven

---

## 🗄 Database Schema

### Tables
- `users`: holds login credentials & roles
- `student`: each user has a linked student record
- `course`: available courses
- `student_courses`: join table for many-to-many relation

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-org/school-app.git
cd school-app
```

### 2. Set up PostgreSQL
Create a database named schooldb:

```bash
CREATE DATABASE schooldb;
```

### 3. Configure application-dev.yml
Update your DB credentials:

```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/schooldb
    username: postgres
    password: your_password
```

### 4. Run the application

```bash
mvn spring-boot:run
```
