terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.23.0"
    }
  }
  backend "s3" {
    bucket         = "api-accounting-tf-state"
    key            = "tf-state-setup"
    region         = "us-east-2"
    encrypt        = true
    dynamodb_table = "api-accounting-tf-lock"
  }
}

provider "aws" {
  region = "us-east-2"
  default_tags {
    tags = {
      Environment = terraform.workspace
      Project     = var.project
      contact     = var.contact
      ManagedBy   = "Terraform/setup"
    }
  }
}
