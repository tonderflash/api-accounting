# Ultra Accounting API

A Spring Boot 3 RESTful service that powers the **Ultra Accounting** ledger. The project is container-first and infrastructure-as-code driven so that the exact same artefacts run in development, CI and production.

---

## Table of Contents
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Prerequisites](#prerequisites)
4. [Running Locally](#running-locally)
   * [With Maven](#with-maven)
   * [With Docker Compose](#with-docker-compose)
5. [Running the Test Suite](#running-the-test-suite)
6. [API Documentation](#api-documentation)
7. [Environment Variables](#environment-variables)
8. [Deployment Images](#deployment-images)
9. [Project Structure](#project-structure)

---

## Features
* RESTful CRUD endpoints for accounting transactions
* Validation with Jakarta Bean Validation
* Database persistence with Spring Data JPA & PostgreSQL
* Auto generated OpenAPI/Swagger UI documentation
* Containerised runtime built with **multi-stage** `Dockerfile`
* Reverse-proxy front-end with NGINX for HTTPS termination & routing
* GitHub Actions pipeline for tests, linting and image publishing
* Terraform IaC to provision AWS (ECR, ECS, RDS, VPC, etc.)

## Tech Stack
| Layer            | Technology                                   |
|------------------|----------------------------------------------|
| Language         | Java 17                                      |
| Framework        | Spring Boot 3                                |
| Build            | Maven 3.9                                    |
| Database         | PostgreSQL 13                                |
| Persistence      | Spring Data JPA / Hibernate                  |
| API Docs         | springdoc-openapi 2                          |
| Containers       | Docker / Docker Compose                      |
| Reverse Proxy    | nginx-unprivileged 1-alpine                  |
| IaC              | Terraform 1.8 + AWS provider 5               |
| CI / CD          | GitHub Actions                               |

## Prerequisites
* **Java 17** & **Maven 3.9** _(only if you want to run without Docker)_
* **Docker** & **Docker Compose v2**
* (Optional) PostgreSQL client CLI for inspecting the database

---

## Running Locally
### With Maven
This requires Java 17 and Maven available on your host:

```bash
# start an ephemeral postgres in docker for convenience
docker run --rm -d \
  --name postgres-dev \
  -e POSTGRES_DB=ultra_accounting \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:13

# run the API (dev profile)
SPRING_PROFILES_ACTIVE=dev \
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ultra_accounting \
SPRING_DATASOURCE_USERNAME=postgres \
SPRING_DATASOURCE_PASSWORD=postgres \
mvn spring-boot:run
```

The service will be available at <http://localhost:8080>.

### With Docker Compose  (Recommended)
Everything – application, proxy and database – is started with a single command:

```bash
# build images & start stack
docker compose up --build
```

Services & Ports
| Service | Image                             | Host Port | Container Port |
|---------|-----------------------------------|-----------|----------------|
| NGINX   | `api-accounting-proxy`            | **8000**  | 8000           |
| API     | `api-accounting-app`              | 8081      | 8080           |
| DB      | `postgres:13`                     | 5432      | 5432           |

Open the API through the proxy at **<http://localhost:8000>**.

Stop & clean up:
```bash
docker compose down -v   # remove volumes
```

## Running the Test Suite
```bash
mvn test
```
Tests are executed automatically in CI on every push to `main`.

## API Documentation
Once the application is running, navigate to:

* Swagger UI  →  <http://localhost:8000/swagger-ui.html>
* OpenAPI JSON →  <http://localhost:8000/v3/api-docs>

## Environment Variables
| Variable                     | Default (dev compose)                 | Description |
|------------------------------|----------------------------------------|-------------|
| `SPRING_PROFILES_ACTIVE`     | `dev`                                  | Spring profile to activate |
| `SPRING_DATASOURCE_URL`      | `jdbc:postgresql://db:5432/ultra_accounting` | JDBC connection string |
| `SPRING_DATASOURCE_USERNAME` | `postgres`                              | DB user |
| `SPRING_DATASOURCE_PASSWORD` | `postgres`                              | DB password |

Additional variables are used in CI/CD & Terraform. See **infra/README.md**.

## Deployment Images
The production compose file `docker-compose-deploy.yml` expects images that are already pushed to AWS ECR:
```bash
docker build -t "$ECR_REGISTRY/api-accounting-app:latest" .
docker build -t "$ECR_REGISTRY/api-accounting-proxy:latest" ./proxy

docker push "$ECR_REGISTRY/api-accounting-app:latest"
docker push "$ECR_REGISTRY/api-accounting-proxy:latest"
```
When deployed, the stack will be reachable via an Application Load Balancer fronting the NGINX container.

## AWS Architecture

The following diagram illustrates the AWS infrastructure architecture used for deploying this application:

![AWS Architecture Diagram](docs/images/aws-architecture.png)

> **Note:** Para ver este diagrama correctamente, guarda la imagen compartida en: `docs/images/aws-architecture.png`

The architecture leverages:
- **VPC** with public and private subnets across multiple AZs
- **Application Load Balancer** for request routing
- **ECS Fargate** for containerized services (app and proxy)
- **RDS PostgreSQL** database in a private subnet
- **S3** for Terraform state and static assets
- **ECR** repositories for Docker images
- **Route 53** for DNS management
- **ACM** for TLS certificates
- **Systems Manager** for secrets management

## Infrastructure TODO List

The following components are pending implementation in our infrastructure:

- [ ] Configure CloudWatch alarms for RDS and ECS services
- [ ] Set up WAF for API protection
- [ ] Implement enhanced VPC Flow Logs
- [ ] Complete IAM permission configuration for RDS DB Subnet Group creation
- [ ] Set up S3 bucket for application logs
- [ ] Configure AWS Backup for RDS automated snapshots
- [ ] Implement CloudWatch Dashboard for service monitoring
- [ ] Set up CloudTrail for API usage tracking
- [ ] Create AWS budget alarms
- [ ] Fix environment variable propagation for local Terraform runs

## Project Structure
```
api-accounting/
├─ src/                  # Java source code
├─ proxy/                # nginx reverse-proxy (Dockerfile, conf, scripts)
├─ infra/                # Terraform configuration
│  ├─ setup/             # one-time foundational resources (ECR, IAM…)
│  └─ deploy/            # per-environment application stack (VPC, ECS, RDS…)
├─ .github/              # GitHub Workflows
├─ docker-compose.yml    # local dev stack
├─ docker-compose-deploy.yml # production compose used by ECS
├─ docs/images/          # Documentation assets including architecture diagrams
└─ Dockerfile            # multi-stage build for the API image
```

---
Happy hacking :rocket:
