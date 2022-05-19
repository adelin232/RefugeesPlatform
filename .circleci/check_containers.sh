#!/bin/bash

# Colours
NORMAL=$(tput sgr0)
RED=$(tput setaf 1)
GREEN=$(tput setaf 2)

# Get container names from docker-compose
mapfile -t container_names < <(cat docker-compose.yml | grep "container_name" | cut -d ":" -f2 | tr -d " ")

# Get proxy
mapfile -t proxy_names < <(cat .env | grep "PROXY_ADDRESS" | cut -d "_" -f1)
mapfile -t proxy_address < <(cat .env | grep "PROXY_ADDRESS" | cut -d "=" -f2)

# Get env variables
MYSQLDB_USER=$(cat .env | grep "MYSQLDB_USER" | cut -d "=" -f2 | tr -d "\n")
MYSQLDB_ROOT_PASSWORD=$(cat .env | grep "MYSQLDB_ROOT_PASSWORD" | cut -d "=" -f2 | tr -d " \n")

SPRING_RABBITMQ_USERNAME=$(cat .env | grep "SPRING_RABBITMQ_USERNAME" | cut -d "=" -f2 | tr -d "\n")
SPRING_RABBITMQ_PASSWORD=$(cat .env | grep "SPRING_RABBITMQ_PASSWORD" | cut -d "=" -f2 | tr -d "\n")

# Sleep for 30 seconds
echo "Sleeping for 30 seconds..."
sleep 30

err=0

echo "Check containers status:"
# Iterate through array
for name in ${container_names[@]};
do
    echo ""
    if (($( docker ps -a | grep $name | wc -l ) > 0 ));
    then
        printf "%-40s%s\n" "--> [$name]" "[${GREEN}✔${NORMAL} ]"

        # Get container status
        STATUS=$(docker inspect --format="{{.State.Status}}" $name | tr '[a-z]' '[A-Z]')
    
        if [ $STATUS == "RUNNING" ]
        then
            # Show status
            printf "%-40s %s\n" "      - Status: $STATUS" "${GREEN}✓${NORMAL}"
            
            # Check mysql
            if [ "$name" == "mysqldb" ]
            then
                if (( $(docker exec $name mysql -u $MYSQLDB_USER -p$MYSQLDB_ROOT_PASSWORD -e "SELECT @@VERSION;" 2>&1 | grep "Error" -c) >= 1 ))
                then
                    printf "%-40s %s\n" "      - Database is up" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                else
                    printf "%-40s %s\n" "      - Database is up" "${GREEN}✓${NORMAL}"
                fi
            fi
            
            # Check adminer
            if [ "$name" == "adminer" ]
            then
                if (( $(curl -L -k -s -o /dev/null -w "%{http_code}" http://localhost:6969) != 200 ))
                then
                    printf "%-40s %s\n" "      - Adminer interface is up" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                else
                    printf "%-40s %s\n" "      - Adminer interface is up" "${GREEN}✓${NORMAL}"
                fi
            fi

            # Check portainer
            if [ "$name" == "portainer" ]
            then
                if (( $(curl -L -k -s -o /dev/null -w "%{http_code}" https://localhost:9443) != 200 ))
                then
                    printf "%-40s %s\n" "      - Portainer interface is up" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                else
                    printf "%-40s %s\n" "      - Portainer interface is up" "${GREEN}✓${NORMAL}"
                fi
            fi

            # Check grafana
            if [ "$name" == "grafana" ]
            then
                if (( $(curl -L -k -s -o /dev/null -w "%{http_code}" https://localhost:3000) != 200 ))
                then
                    printf "%-40s %s\n" "      - Grafana interface is up" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                else
                    printf "%-40s %s\n" "      - Grafana interface is up" "${GREEN}✓${NORMAL}"
                fi
            fi

            # Check nginx-proxy
            if [ "$name" == "nginx-proxy" ]
            then
                for i in "${!proxy_names[@]}";
                do
                    if [ $(curl -k -I "https://${proxy_address[$i]}" 2>&1 | grep "server: nginx" | wc -l) != 0 ]
                    then
                        printf "%-40s %s\n" "      - ${proxy_names[$i]^} proxy is up" "${GREEN}✓${NORMAL}"
                    else
                        printf "%-40s %s\n" "      - ${proxy_names[$i]^} proxy is up" "${RED}x${NORMAL}"
                        docker logs $name
                        err=-1
                    fi
                done
            fi

            # Check rabbitmq-broker
            if [ "$name" == "rabbitmq-broker" ]
            then
                if (( $(curl -L -k -s -o /dev/null -w "%{http_code}" http://localhost:15672) != 200 ))
                then
                    printf "%-40s %s\n" "      - Rabbitmq interface is up" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                else
                    printf "%-40s %s\n" "      - Rabbitmq interface is up" "${GREEN}✓${NORMAL}"
                fi
                if (( $(curl -u guest:rabbitmq http://localhost:15672/api/connections 2>&1 | wc -l) != 0 ))
                then
                    printf "%-40s %s\n" "      - Has rabbitmq connections" "${GREEN}✓${NORMAL}"
                else
                    printf "%-40s %s\n" "      - Has rabbitmq connections" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                fi
            fi

            # Check rabbitmq-listener
            if [ "$name" == "rabbitmq-listener" ]
            then
                if (( $(curl -u $SPRING_RABBITMQ_USERNAME:$SPRING_RABBITMQ_PASSWORD http://localhost:15672/api/connections 2>&1 | grep "Python_pika_connection" | wc -l) != 0 ))
                then
                    printf "%-40s %s\n" "      - Rabbitmq connection" "${GREEN}✓${NORMAL}"
                else
                    printf "%-40s %s\n" "      - Rabbitmq connection" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                fi
            fi

            # Check spring
            if [ "$name" == "spring-app" ]
            then
                if (( $(curl -L -k -s -o /dev/null -w "%{http_code}" https://localhost:8443) != 200 ))
                then
                    printf "%-40s %s\n" "      - Spring website is up" "${RED}x${NORMAL}"
                    docker logs $name
                    err=-1
                else
                    printf "%-40s %s\n" "      - Spring website is up" "${GREEN}✓${NORMAL}"
                fi
            fi
        else
            printf "%-40s %s\n" "      - Status: $STATUS" "${RED}x${NORMAL}"
            docker logs $name
            err=-1
        fi
    else
        printf "%-40s%s\n" "--> [$name]" "(${RED}✖${NORMAL} )"
        err=-1
    fi
done

# return error
exit $err