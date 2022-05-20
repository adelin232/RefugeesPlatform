#!/bin/bash

# Colours
NORMAL=$(tput sgr0)
RED=$(tput setaf 1)
GREEN=$(tput setaf 2)

# Create logs folder
mkdir -p /tmp/test-logs/spring-tests

# Set error variable
err=0

# Run tests
#docker exec spring-app mvn test > /tmp/test-logs/spring-tests/test.log

# Check errors
if (( $(cat /tmp/test-logs/spring-tests/test.log | grep "Error" | wc -l) != 0 ));
then
    printf "Failures: %s\n" "$(cat /tmp/test-logs/spring-tests/test.log | grep "Failures:" | tail -n1 | sed 's/.*Failures: //' | cut -d " " -f1 | tr -d ",")"
    #err=-1
fi

# return error
exit $err