#!/bin/bash
sudo yum update -y
sudo yum install docker -y
sudo systemctl enable docker.service
sudo service docker start

sudo docker pull gustosilva/music-api-v2:latest

sudo docker run -d \
  --env AWS_SIGNINGREGION=us-east-1 \
  --env AWS_ENDPOINT_DYNAMO=https://dynamodb.us-east-1.amazonaws.com \
  --env AWS_ENDPOINT_SECRETS_MANAGER=https://secretsmanager.us-east-1.amazonaws.com \
  --env ACTIVE_PROFILE=ec2 \
  -p 80:8080 gustosilva/music-api-v2:latest