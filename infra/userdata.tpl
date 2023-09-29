#!/bin/bash
sudo apt-get update -y &&
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common &&
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - &&
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" &&
sudo apt-get update -y &&
sudo sudo apt-get install docker-ce docker-ce-cli containerd.io -y &&
sudo usermod -aG docker ubuntu
sudo systemctl enable docker.service
sudo systemctl enable containerd.service

docker pull gustosilva/music-api-v2:latest

docker run \
  --env AWS_ACCESSKEY=AWS_ACCESSKEY \
  --env AWS_SECRETKEY=AWS_SECRETKEY \
  --env AWS_SIGNINGREGION=AWS_SIGNINGREGION \
  -p 8080:8080 gustosilva/music-api-v2:latest