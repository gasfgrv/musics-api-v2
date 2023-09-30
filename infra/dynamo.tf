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

resource "aws_vpc_endpoint" "aws_vpc_endpoint_dynamodb" {
  service_name      = "com.amazonaws.us-east-1.dynamodb"
  vpc_id            = aws_vpc.musics_vpc.id
  route_table_ids   = [aws_route_table.musics_route_table.id]
  vpc_endpoint_type = "Gateway"
  tags = {
    Name = "dynamodb_vpc_endpoint"
  }
}
