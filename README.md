# Ecommerce — Spring Boot REST API

> A small but serious e-commerce backend: JWT-secured REST API with
> role-based access, product catalog, shopping cart and order management.

Built with **Spring Boot 2.7** and **PostgreSQL** as a learning project to
explore stateless JWT authentication, custom security filters and a clean
controller/service/repository layering.

## Features

- **Stateless JWT auth** — login, access + refresh tokens (`auth0/java-jwt`),
  BCrypt-hashed passwords, custom authentication & authorization filters.
- **Role-based access control** — `ROLE_ADMIN` and `ROLE_USER` enforced via
  `@PreAuthorize` method security.
- **Product catalog** — CRUD, "hot" products, pageable + filterable listing
  (sort by field, page size).
- **Shopping cart** — add / remove / set quantity, then check out the whole cart.
- **Orders** — users view their own orders; admins list all purchases and mark
  them as fulfilled.

## Tech stack

| Layer | Technology |
|-------|-----------|
| Language | Java 8 |
| Framework | Spring Boot 2.7 (Web, Data JPA, Security) |
| Auth | Spring Security + `auth0/java-jwt`, BCrypt |
| Persistence | PostgreSQL + Hibernate/JPA |
| Build | Maven (wrapper included) |
| Misc | Lombok |

## API overview

| Base path | Responsibility |
|-----------|----------------|
| `/users` | Registration, cart operations, profile, token refresh |
| `/products` | Catalog CRUD (admin), browse & buy (user) |
| `/purchase` | Order history (user), order management (admin) |
| `/role` | Role creation and assignment (admin) |

## Getting started

### Prerequisites
- JDK 8, Maven (or use the bundled `./mvnw`)
- A running PostgreSQL instance

### Configuration
Set the datasource and JWT secret via environment variables (do **not** commit
real credentials):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

### Run
```bash
./mvnw spring-boot:run
```
The API starts on `http://localhost:8080`.

## Project status

Learning project (v0.0.1-alpha), not maintained for production use.

## Companion project

A Flutter front-end for this API lives in
[`ecommerce-view`](https://github.com/simoneloop/ecommerce-view).
