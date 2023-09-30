##!/bin/bash
sudo yum install docker

docker pull gustosilva/music-api-v2:latest

docker run -d \
  --env AWS_SIGNINGREGION=us-east-1 \
  -p 80:8080 gustosilva/music-api-v2:latest