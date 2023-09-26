resource "aws_dynamodb_table" "songs_table" {
  name         = "MusicsTb"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "MusicId"
  range_key    = "MusicName"

  attribute {
    name = "MusicId"
    type = "S"
  }

  attribute {
    name = "MusicName"
    type = "S"
  }
}
