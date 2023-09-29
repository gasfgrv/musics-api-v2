variable "spotify_client" {
  description = "Spotify Client"
  type        = string
  sensitive   = true
}

variable "spotify_secret" {
  description = "Spotify Secret"
  type        = string
  sensitive   = true
}

resource "aws_secretsmanager_secret" "spotify_api_client_id" {
  name = "spotify_api_client_id"
}

resource "aws_secretsmanager_secret_version" "spotify_client_id" {
  secret_id     = aws_secretsmanager_secret.spotify_api_client_id.id
  secret_string = var.spotify_client
}

resource "aws_secretsmanager_secret" "spotify_api_client_secret" {
  name = "spotify_api_client_secret"
}

resource "aws_secretsmanager_secret_version" "spotify_client_secret" {
  secret_id     = aws_secretsmanager_secret.spotify_api_client_secret.id
  secret_string = var.spotify_secret
}