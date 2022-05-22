#!/bin/bash

######################### 
#                       #
#  ALWAYS RUN AS SUDO   #
#                       #
#########################

# set user and group id
export UID_GID="$(id -u):$(id -g)"

# stop running containers
docker-compose down --remove-orphans
echo ""
sleep 1

# install plugins
if ! [ "$(docker plugin inspect loki)" ];
then
    docker plugin install grafana/loki-docker-driver:2.5.0 --alias loki --grant-all-permissions
fi

# start new containers
docker-compose up --detach

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
