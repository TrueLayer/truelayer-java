alias b := build
alias l := lint
alias ut := unit-test
alias it := integration-test
alias at := acceptance-test

build:
    ./gradlew build -x test

lint:
    ./gradlew spotlessApply

unit-test:
    ./gradlew unit-tests

integration-test:
    ./gradlew integration-tests

acceptance-test:
    echo "Staring acceptance-tests with client_id=[$TL_CLIENT_ID]..."
    ./gradlew acceptance-tests