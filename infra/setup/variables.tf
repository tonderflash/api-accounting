variable "tf_state_bucket" {
  description = "S3 bucket for TF state"
  default     = "api-accounting-tf-state"
}

variable "tf_state_lock_table" {
  description = "DynamoDB table for TF state lock"
  default     = "api-accounting-tf-lock"
}

variable "project" { default = "api-accounting-api" }
variable "contact" { default = "you@example.com" }
