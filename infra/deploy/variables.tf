variable "prefix" {
  description = "Prefix for naming resources"
  default     = "apiacct"
}

variable "project" {
  description = "Project name"
  default     = "api-accounting-api"
}

variable "contact" {
  description = "Contact e-mail"
  default     = "g594@hotmail.com"
}

variable "db_username" {
  description = "Username for the recipe app api database"
  default     = "accountingapp"
}

variable "db_password" {
  description = "Password for the Terraform database"
}

variable "ecr_proxy_image" {
  description = "Path to the ECR repo with the proxy image"
}

variable "ecr_app_image" {
  description = "Path to the ECR repo with the API image"
}

variable "subdomain" {
  description = "Subdomain for each environment"
  type        = map(string)

  default = {
    prod    = "api"
    staging = "api.staging"
    dev     = "api.dev"
  }
}
