#!/bin/bash

# stop running containers
docker-compose down --remove-orphans
echo ""
sleep 1

# install plugins
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions

# remove database files
sudo rm -rf ./mysql/mysql-data/*

# start new containers
docker-compose up --force-recreate --detach --build
echo ""
sleep 1

# add hostnames to /etc/hosts

for _file in /etc/hosts; do
    if ! grep -qF '127.0.0.1        local.proxy.spring' $_file; then
        sudo echo "127.0.0.1        local.proxy.spring" >> $_file
    fi
    if ! grep -qF '127.0.0.1        local.proxy.adminer' $_file; then
        sudo echo "127.0.0.1        local.proxy.adminer" >> $_file
    fi
    if ! grep -qF '127.0.0.1        local.proxy.portainer' $_file; then
        sudo echo "127.0.0.1        local.proxy.portainer" >> $_file
    fi
done

# show running containers
docker ps
