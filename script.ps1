docker-compose down --remove-orphans
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
rm -r ./mysql/mysql-data/*
rm -r ./grafana/grafana-storage/*
docker-compose up --force-recreate --detach --build
echo ""
sleep 1
docker ps
