docker-compose down --remove-orphans
docker-compose up --force-recreate --detach --build
echo ""
sleep 1
docker ps
