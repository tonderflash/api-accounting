variable "tf_state_bucket" {
  description = "S3 bucket for TF state"
  default     = "api-accounting-app-tf-state"
}

variable "tf_state_lock_table" {
  description = "DynamoDB table for TF state lock"
  default     = "accounting-app-api-tf-lock"
}

variable "project" { default = "api-accounting-api" }
variable "contact" { default = "g594@hotmail.com" }
