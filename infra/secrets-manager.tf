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
  name                    = "spotify_client_id"
  description             = "Spotify Client Id"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "spotify_client_id" {
  secret_id     = aws_secretsmanager_secret.spotify_api_client_id.id
  secret_string = var.spotify_client
}

resource "aws_secretsmanager_secret" "spotify_api_client_secret" {
  name                    = "spotify_client_secret"
  description             = "Spotify Client Secret"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "spotify_client_secret" {
  secret_id     = aws_secretsmanager_secret.spotify_api_client_secret.id
  secret_string = var.spotify_secret
}

resource "aws_vpc_endpoint" "aws_vpc_endpoint_secretsmanager" {
  service_name        = "com.amazonaws.us-east-1.secretsmanager"
  vpc_id              = aws_vpc.musics_vpc.id
  subnet_ids          = [aws_subnet.musics_subnet.id]
  security_group_ids  = [aws_security_group.aws_security_group_secretsmanager.id]
  vpc_endpoint_type   = "Interface"
  private_dns_enabled = true
  tags = {
    Name = "secretsmanager_vpc_endpoint"
  }
}

resource "aws_security_group" "aws_security_group_secretsmanager" {
  name        = "secretsmanager_api_sg"
  description = "Security group para acessar o Secrets Manager"
  vpc_id      = aws_vpc.musics_vpc.id
}

resource "aws_security_group_rule" "secretsmanager_security_group_rule_out" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.aws_security_group_secretsmanager.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "secretsmanager_security_group_rule_in" {
  from_port         = 22
  protocol          = "tcp"
  security_group_id = aws_security_group.aws_security_group_secretsmanager.id
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "secretsmanager_security_group_rule_http_in" {
  from_port         = 443
  protocol          = "tcp"
  security_group_id = aws_security_group.aws_security_group_secretsmanager.id
  to_port           = 443
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}
