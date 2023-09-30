#!/bin/bash
sudo yum update -y
sudo yum install docker -y
sudo systemctl enable docker.service
sudo service docker start

sudo docker pull gustosilva/music-api-v2:latest

sudo docker run -d \
  --env AWS_SIGNINGREGION=us-east-1 \
  -p 80:8080 gustosilva/music-api-v2:latest