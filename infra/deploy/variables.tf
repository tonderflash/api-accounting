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
