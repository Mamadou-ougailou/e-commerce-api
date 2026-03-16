# E-Commerce API

RESTful e-commerce backend with Spring Boot, JWT auth, and product management.

## Stack
- Java 21, Spring Boot 3.4.3
- Spring Security + JWT (JJWT)
- Spring Data JPA (Hibernate), PostgreSQL 16
- MapStruct, Maven, Docker Compose

## Quick Start (Docker)
```bash
docker compose up --build -d
```
API: http://localhost:8080  |  DB: localhost:5432

## Local Run
1. Create PostgreSQL DB `ecommerce`.
2. Check `src/main/resources/application.properties`.
3. Start app:
```bash
mvn spring-boot:run
```

## Auth
- Public: `/auth/**`, `/api/public/**`
- Protected: all other routes (Bearer JWT)
- Header: `Authorization: Bearer <token>`

Auth endpoints:
- `POST /auth/register`
- `POST /auth/login`

## Product Endpoints
Public:
- `GET /api/public/products`
- `GET /api/public/products/{id}`
- `GET /api/public/products/featured`
- `GET /api/public/products/search?query=&category=&minPrice=&maxPrice=&page=0&size=10`

Admin (JWT required):
- `POST /api/admin/products/create`
- `PATCH /api/admin/products/{id}`
- `DELETE /api/admin/products/{id}`
- `GET /api/admin/products/count`
- `PATCH /api/admin/products/{id}/image` (`multipart/form-data`, field: `file`)

## Build & Test
`mvn clean package && mvn test`
