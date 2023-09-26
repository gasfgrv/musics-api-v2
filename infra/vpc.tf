#
#resource "aws_vpc" "songs_vpc" {
#  cidr_block           = "10.0.0.0/16"
#  enable_dns_hostnames = true
#  enable_dns_support   = true
#
#  tags = {
#    "Name" = "Songs API VPC"
#  }
#}
#
#resource "aws_subnet" "songs_subnet" {
#  vpc_id                  = aws_vpc.songs_vpc.id
#  cidr_block              = "10.0.1.0/24"
#  availability_zone       = "us-east-1a"
#  map_public_ip_on_launch = true
#
#  tags = {
#    "Name" = "Songs API Subnet"
#  }
#}
#
#resource "aws_internet_gateway" "songs_internet_gateway" {
#  vpc_id = aws_vpc.songs_vpc.id
#
#  tags = {
#    "Name" = "Songs API Internet Gateway"
#  }
#}
#
#resource "aws_route_table" "songs_route_table" {
#  vpc_id = aws_vpc.songs_vpc.id
#
#  tags = {
#    "Name" = "Songs API Route Table"
#  }
#}
#
#resource "aws_route" "terraform_route" {
#  route_table_id         = aws_route_table.songs_route_table.id
#  destination_cidr_block = "0.0.0.0/0"
#  gateway_id             = aws_internet_gateway.songs_internet_gateway.id
#}
#
#resource "aws_route_table_association" "terraform_route_table_association" {
#  route_table_id = aws_route_table.songs_route_table.id
#  subnet_id      = aws_subnet.songs_subnet.id
#}
