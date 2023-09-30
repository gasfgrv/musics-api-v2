data "aws_iam_policy_document" "music_policy_document" {
  version = "2012-10-17"
  statement {
    sid = "allowDynamoDb"
    actions = [
      "dynamodb:GetItem",
      "dynamodb:PutItem",
      "dynamodb:Query",
      "dynamodb:Scan"
    ]
    effect    = "Allow"
    resources = [aws_dynamodb_table.songs_table.arn]
  }
  statement {
    sid = "allowSecretsManager"
    actions = [
      "secretsmanager:GetSecretValue"
    ]
    effect = "Allow"
    resources = [
      aws_secretsmanager_secret.spotify_api_client_secret.arn,
      aws_secretsmanager_secret.spotify_api_client_id.arn
    ]
  }
}

resource "aws_iam_policy" "music_policy" {
  name   = "MusicApiAccessPolicy"
  policy = data.aws_iam_policy_document.music_policy_document.json
}

resource "aws_iam_role" "music_role" {
  name = "MusicApiAccessRole"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "secretsmanager_role_policy_attachment" {
  role       = aws_iam_role.music_role.name
  policy_arn = aws_iam_policy.music_policy.arn
}

resource "aws_iam_instance_profile" "music_profile" {
  name = "test_profile"
  role = aws_iam_role.music_role.name
}
