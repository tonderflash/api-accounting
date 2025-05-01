output "cd_user_access_key_id" {
  value = aws_iam_access_key.cd.id
}

output "cd_user_access_key_secret" {
  value     = aws_iam_access_key.cd.secret
  sensitive = true
}

output "ecr_repo_app" {
  value = aws_ecr_repository.app.repository_url
}

output "ecr_repo_proxy" {
  value = aws_ecr_repository.proxy.repository_url
}
