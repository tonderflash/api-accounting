terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.23.0"
    }
  }
  backend "s3" {
    bucket               = "api-accounting-app-tf-state"
    key                  = "tf-state-deploy"
    workspace_key_prefix = "tf-state-deploy-env"
    region               = "us-east-2"
    encrypt              = true
    dynamodb_table       = "accounting-app-api-tf-lock"
  }
}

provider "aws" {
  region = "us-east-2"
  default_tags {
    tags = {
      Environment = terraform.workspace
      Project     = var.project
      contact     = var.contact
      ManagedBy   = "Terraform/deploy"
    }
  }
}

# Data source para obtener la región AWS actual
data "aws_region" "current" {}

locals {
  prefix = "${var.prefix}-${terraform.workspace}"
}
