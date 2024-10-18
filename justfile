alias b := build
alias f := format
alias ut := unit-test
alias it := integration-test
alias at := acceptance-test
alias t := test

build:
    ./gradlew build -x test

format:
    ./gradlew spotlessApply

unit-test:
    ./gradlew unit-tests

integration-test:
    ./gradlew integration-tests

acceptance-test:
    echo "Staring acceptance-tests with client_id=[$TL_CLIENT_ID]..."
    ./gradlew acceptance-tests

test: unit-test integration-test acceptance-test