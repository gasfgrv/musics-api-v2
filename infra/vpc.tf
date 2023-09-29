resource "aws_vpc" "musics_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    "Name" = "musics API VPC"
  }
}

resource "aws_subnet" "musics_subnet" {
  vpc_id                  = aws_vpc.musics_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    "Name" = "musics API Subnet"
  }
}

resource "aws_internet_gateway" "musics_internet_gateway" {
  vpc_id = aws_vpc.musics_vpc.id

  tags = {
    "Name" = "musics API Internet Gateway"
  }
}

resource "aws_route_table" "musics_route_table" {
  vpc_id = aws_vpc.musics_vpc.id

  tags = {
    "Name" = "musics API Route Table"
  }
}

resource "aws_route" "terraform_route" {
  route_table_id         = aws_route_table.musics_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.musics_internet_gateway.id
}

resource "aws_route_table_association" "terraform_route_table_association" {
  route_table_id = aws_route_table.musics_route_table.id
  subnet_id      = aws_subnet.musics_subnet.id
}
