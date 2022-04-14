docker-compose down --remove-orphans
rm -r ./mysql/mysql-data/*
docker-compose up --force-recreate --detach --build
echo ""
sleep 1
docker ps
