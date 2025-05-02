# Infrastructure as Code – Ultra Accounting API

This directory contains **Terraform** configuration to provision all cloud resources required to run the application on AWS.

There are **two separate root modules**:

| Module | Path            | Purpose |
|--------|-----------------|---------|
| `setup`  | `infra/setup`   | One-time foundational resources shared by all environments (ECR repos, IAM roles & policies, S3 state bucket, DynamoDB lock table). |
| `deploy` | `infra/deploy`  | Environment-specific application stack (VPC, ECS Fargate cluster, ALB, RDS, Secrets, Route 53). |

---

## Prerequisites
* Terraform **1.8**
* AWS account & credentials with sufficient privileges (or an assumed role)
* [Docker] if you prefer to run Terraform inside a container (a compose service is provided)

### Back-End State
Both modules use **remote S3 state** with DynamoDB locking to avoid concurrent execution issues. Bucket/table names are defined in [`setup/variables.tf`](setup/variables.tf).

---

## Module: `setup`
Run **once** per AWS account. It creates resources that rarely change.

```bash
cd infra/setup
terraform init
terraform workspace new dev   # or use an existing workspace
terraform apply -var "contact=<you@company.com>"
```

Outputs include the ARNs of IAM roles that are later referenced by the `deploy` module.

---

## Module: `deploy`
This module should be executed **per environment** (dev, staging, prod). The workspace name determines environment-specific naming and Route 53 sub-domain.

```bash
cd infra/deploy
terraform init
terraform workspace select dev || terraform workspace new dev

# required variables
export TF_VAR_db_password=superSecret123!
export TF_VAR_ecr_proxy_image="<account>.dkr.ecr.<region>.amazonaws.com/api-accounting-proxy:latest"
export TF_VAR_ecr_app_image="<account>.dkr.ecr.<region>.amazonaws.com/api-accounting-app:latest"

terraform apply -auto-approve
```

Key resources created:
* **VPC** with public & private subnets across 2 AZs
* **RDS PostgreSQL** (multi-AZ in prod)
* **ECS Fargate** service (one task for proxy, one for app)
* **Application Load Balancer** listening on ports 80/443
* **ACM** certificate (prod & staging)
* **Route 53** DNS record pointing to the ALB

---

## Running Terraform via Docker Compose
A helper compose service is defined in [`infra/docker-compose.yml`](docker-compose.yml):

```bash
# from project root
docker compose -f infra/docker-compose.yml run --rm terraform bash
```

Inside the container you will find the `setup` and `deploy` directories mounted at `/tf`.

Ensure your AWS credentials are exported on the host so they are passed through.

---

## Environment Variables
Besides standard AWS vars (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, …) the `deploy` module expects:

| Variable | Description |
|----------|-------------|
| `TF_VAR_db_password` | Master password for the RDS instance |
| `TF_VAR_ecr_proxy_image` | Full image URI of the NGINX proxy |
| `TF_VAR_ecr_app_image` | Full image URI of the Spring Boot app |
| `TF_WORKSPACE` | Terraform workspace (dev / staging / prod) |

---

## Useful Commands
```bash
# switch workspace
terraform workspace select prod

# plan against specific workspace without switching
terraform plan -workspace=staging

# destroy an environment
terraform destroy -auto-approve
```

---

_Questions / issues? Open an issue or ping the infra team._
