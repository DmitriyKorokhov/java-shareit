#!/bin/bash

mvn clean install

docker container rm gateway
docker container rm shareit_server

docker image rm gateway
docker image rm shareit_server

docker-compose up
