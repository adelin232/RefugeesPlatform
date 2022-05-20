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
echo "Running spring tests..."
docker exec spring-app mvn test > /tmp/test-logs/spring-tests/test.log

#  Get test names
mapfile -t test_name < <(cat /tmp/test-logs/spring-tests/test.log | grep "Running com.project." | sed 's/.*\.//')
mapfile -t test_failures < <(cat /tmp/test-logs/spring-tests/test.log | grep "Tests run:" | sed 's/.*Failures: //' | cut -d " " -f1 | tr -d ",")
mapfile -t test_errors < <(cat /tmp/test-logs/spring-tests/test.log | grep "Tests run:" | sed 's/.*Errors: //' | cut -d " " -f1 | tr -d ",")
mapfile -t test_count < <(cat /tmp/test-logs/spring-tests/test.log | grep "Tests run:" | sed 's/.*Tests run: //' | cut -d " " -f1 | tr -d ",")
mapfile -t test_skips < <(cat /tmp/test-logs/spring-tests/test.log | grep "Tests run:" | sed 's/.*Skipped: //' | cut -d " " -f1 | tr -d ",")

# Check errors
if (( $(cat /tmp/test-logs/spring-tests/test.log | grep "Error" | wc -l) != 0 ));
then
    # Show tests run
    printf "%-29s" " -> Tests run: ${test_count[-1]}"
    if (( ${test_count[-1]} != 0 ));
    then
        printf "%s\n" "[${GREEN}✔${NORMAL} ]"
    else
        printf "%s\n" "(${RED}✖${NORMAL} )"
    fi
    # Show failures
    printf "%-29s" " -> Failures: ${test_failures[-1]}"
    if (( ${test_failures[-1]} == 0 ));
    then
        printf "%s\n" "[${GREEN}✔${NORMAL} ]"
    else
        err=-1
        printf "%s\n" "(${RED}✖${NORMAL} )"
    fi
    # Show errors
    printf "%-29s" " -> Errors: ${test_errors[-1]}"
    if (( ${test_errors[-1]} == 0 ));
    then
        printf "%s\n" "[${GREEN}✔${NORMAL} ]"
    else
        err=-1
        printf "%s\n" "(${RED}✖${NORMAL} )"
    fi
    # Show skips
    printf "%-29s\n" " -> Skipped: ${test_skips[-1]}"

    # show individual test results
    printf "\nIndividual test results:\n"
    for i in "${!test_name[@]}";
    do
        echo ""
        printf "%-29s" " --> ${test_name[$i]}"
        if (( ${test_failures[$i]} == 0 )) && (( ${test_errors[$i]} == 0 ));
        then
            printf "%s\n" "[${GREEN}✔${NORMAL} ]"
        else
            printf "%s\n" "(${RED}✖${NORMAL} )"
        fi
        # Show tests run
        printf "%-30s\n" "      - Tests run: ${test_count[$i]}"
        # Show failures
        printf "%-30s" "      - Failures: ${test_failures[$i]}"
        if (( ${test_failures[$i]} == 0 ));
        then
            printf "%s\n" "${GREEN}✓${NORMAL}"
        else
            printf "%s\n" "${RED}x${NORMAL}"
        fi
        # Show errors
        printf "%-30s" "      - Errors: ${test_errors[$i]}"
        if (( ${test_errors[$i]} == 0 ));
        then
            printf "%s\n" "${GREEN}✓${NORMAL}"
        else
            printf "%s\n" "${RED}x${NORMAL}"
        fi
        # Show skips
        printf "%-30s\n" "      - Skipped: ${test_skips[$i]}"


    done
fi

# return error
exit $err