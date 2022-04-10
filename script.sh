#!/bin/bash
docker-compose down
echo ""
sleep 1
sudo rm -rf ./mysql/mysql-data/*
docker-compose up --force-recreate --detach --build
echo ""
sleep 1
docker ps
