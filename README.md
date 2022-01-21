# TrueLayer Java

[![License](https://img.shields.io/:license-mit-blue.svg)](https://truelayer.mit-license.org/)


The official [TrueLayer](https://truelayer.com) Java client provides convenient access to TrueLayer APIs from applications built with Java. 

The library currently targets Java 11.

## Installation


### Unstable releases

Unstable releases are published as Github package within this repository. 

To use on of those release with Gradle, make sure you have the following repository listed in your build.gradle file: 
```gradle
repositories {
    // ... all your existing repos here

    maven {
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/TrueLayer/truelayer-java"
        credentials {
            username = project.findProperty("gpr.user")
            password = project.findProperty("gpr.key")
        }
    }
}
```

Then include a gradle.properties file in your main module which includes your Github username and a personal access token:
 
```properties
# gradle.properties
gpr.user=${your TL username}
gpr.key=${your TL PAT}
```

Please note that the above personal access token should have at least the following scopes: 
- repo
- read:packages
- read:org

Finally make sure you declare the desired library version as dependency of your project: 

```gradle
dependencies {
    // ... your existing dependencies

    // TL Java BE library
    implementation 'truelayer-java:lib:0.0.5'
}
```
### Final releases

TBD

## Documentation

For a comprehensive list of examples, check out the [API documentation](https://docs.truelayer.com).

## Usage

### Prerequisites

First [sign up](https://console.truelayer.com/) for a developer account. Follow the instructions to set up a new application and obtain your Client ID and Secret. Once the application has been created you must add your application redirected URIs in order to test your integration end-to-end. 

Next, generate a signing key pair used to sign API requests.

To generate a private key, run:

```sh
docker run --rm -v ${PWD}:/out -w /out -it alpine/openssl ecparam -genkey -name secp521r1 -noout -out ec512-private-key.pem
```

To obtain the public key, run:

```sh
docker run --rm -v ${PWD}:/out -w /out -it alpine/openssl ec -in ec512-private-key.pem -pubout -out ec512-public-key.pem
```


### Configure Settings


### Initialize TrueLayerClient
```java
var client = TrueLayerClient.New()
        .useSandbox() // optional: to use TL sandbox environment
        .clientCredentials(
                ClientCredentials.builder()
                        .clientId("a-client-id")
                        .clientSecret("a-secret").build())
        .signingOptions(
                SigningOptions.builder()
                        .keyId("a-key-id")
                        .privateKey(Files.readAllBytes(Path.of("my-private-key.pem")))
                        .build())
        .build();
```

### Make a createPaymentResponse
```java
var paymentRequest = CreatePaymentRequest.builder()
        .amountInMinor(101)
        .currency("GBP")
        .paymentMethod(BankTransfer.builder()
                .build())
        .beneficiary(MerchantAccount.builder()
                .id("e83c4c20-b2ad-4b73-8a32-ee855362d72a")
                .build())
        .user(CreatePaymentRequest.User.builder()
                .name("Andrea Di Lisio")
                .type(CreatePaymentRequest.User.Type.NEW)
                .email("andrea@truelayer.com")
                .build())
        .build();        

var paymentResponse = client
    .payments()
    .createPayment(paymentRequest);
    
if(paymentResponse.isError()){
    // inspect the problem details and implement the error handling logic
    var problemDetails = paymentResponse.getError();   
    ...
}else{
    // logic for successful createPaymentResponse execution
    var createPaymentResponse = paymentResponse.getData();
    ...
}    
    
```

### Build a link to our hosted createPaymentResponse page
```java
var hppLink = client.hpp().getHostedPaymentPageLink("your-createPaymentResponse-id",
        "your-createPaymentResponse-token",
        URI.create("http://yourdomain.com"));
```

## Building locally

## Testing
### Unit tests

To run unit tests: 
```sh
./gradlew unit-tests
```

### Integration tests

To run integration tests:
```sh
./gradlew integration-tests
```

### Acceptance tests

To execute tests against TrueLayer sandbox environment, you should set the below environment variables:
- `TL_CLIENT_ID`
- `TL_CLIENT_SECRET`
- `TL_SIGNING_KEY_ID`
- `TL_SIGNING_PRIVATE_KEY`

and finally run:
```sh
./gradlew acceptance-tests
```
## Code linting
To enforce coding style guidelines the project uses [palantir-java-format styles via Spotless gradle plugin](https://github.com/diffplug/spotless/tree/main/plugin-gradle#palantir-java-format).


To locally check that your sources comply with our formatting
```sh
./gradlew spotlessJavaCheck
```

To appy the changes suggested - if any
```sh
./gradlew spotlessApply
```

Bear in mind that the above checks are enforced at CI time, thus 
the builds will fail if not compliant.

When developing on IntelliJ you can optionally install this [Spotless IntelliJ Gradle plugin](https://github.com/ragurney/spotless-intellij-gradle) as well.

## Library Documentation
TBD

## Contributing

Contributions are always welcome!

See [contributing](contributing.md) for ways to get started.

Please adhere to this project's [code of conduct](CODE_OF_CONDUCT.md).

  
## License

[MIT](LICENSE)
