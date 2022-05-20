#!/bin/bash

# !.!.!.!.!.!.!.!.!.!

# ALWAYS RUN AS SUDO

# ^.^.^.^.^.^.^.^.^.^

# stop running containers
docker-compose down --remove-orphans
echo ""
sleep 1

# install plugins
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions

# remove database files
sudo rm -rf ./mysql/mysql-data/*

# remove grafana storage
# sudo rm -rf ./grafana/grafana-storage/*
find ./grafana/grafana-storage -mindepth 1 ! -regex '^./grafana/grafana-storage/plugins\(/.*\)?' -delete

# remove portainer storage
sudo rm -rf ./portainer/portainer-data/*

# give permissions
sudo chmod -R 777 ./

# start new containers
sudo docker-compose up --force-recreate --detach --build

# get proxy names
mapfile -t proxy_address < <(cat .env | grep "PROXY_ADDRESS" | cut -d "=" -f2)

# add proxy names to /etc/hosts
for _file in /etc/hosts; do
    for _proxy in ${proxy_address[@]}; do
        if ! grep -qF '127.0.0.1        $_proxy' $_file; then
            sudo echo "127.0.0.1        $_proxy" >> $_file
        fi
    done
done

# show running containers
sleep 2
echo ""
docker ps -a
