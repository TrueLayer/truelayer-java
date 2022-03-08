# TrueLayer Java

[![Build](https://github.com/TrueLayer/truelayer-java/actions/workflows/build.yml/badge.svg)](https://github.com/TrueLayer/truelayer-java/actions/workflows/build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.truelayer/truelayer-java/badge.svg?style=flat)](https://search.maven.org/artifact/com.truelayer/truelayer-java)
[![Coverage Status](https://coveralls.io/repos/github/TrueLayer/truelayer-java/badge.svg?t=gcGKQv)](https://coveralls.io/github/TrueLayer/truelayer-java)
[![License](https://img.shields.io/:license-mit-blue.svg)](https://truelayer.mit-license.org/)

The official [TrueLayer](https://truelayer.com) Java client provides convenient access to TrueLayer APIs from applications built with Java. 

## Installation

### Stable releases

Our stable releases are hosted on [Maven Central](https://search.maven.org/artifact/com.truelayer/truelayer-java).

As such, it's enough to simply declare the desired `truelayer-java` artifact dependency
:

```gradle
dependencies {
    // ... your existing dependencies

    // TL Java BE library
    implementation 'com.truelayer:truelayer-java:$version'
}
```

To know more about our final releases please see our [Releases](https://github.com/TrueLayer/truelayer-java/releases) section.

### Unstable releases

Unstable releases are published as `SNAPSHOT` releases on the Nexus Sonatype repository. 

To use on of those release with Gradle, make sure you have the following repository listed in your build.gradle file: 
```gradle
repositories {
    // ... all your existing repos here
    
    maven{
        url 'https://s01.oss.sonatype.org/content/repositories/snapshots/'
    }   
}
``` 

And you declare a dependency with a `-SNAPSHOT` suffix:

```gradle
dependencies {
    // ... your existing dependencies

    // TL Java BE library
    implementation 'com.truelayer:truelayer-java:$version-$featureName-SNAPSHOT'
}
```

A sample snapshot artifact would be `com.truelayer:truelayer-java:0.4.10-jsdk-9-SNAPSHOT`, 
where `0.4.10` represents the future final version of the library and `jsdk-9` is the
branch name on which the new feature is being implemented.

There can be multiple artifacts available for a given snapshot. Gradle will automatically look for the latest one.

## Documentation

Check out the [API documentation](https://docs.truelayer.com) and [Java library documentation](https://truelayer.github.io/truelayer-java).

## Usage

### Prerequisites

Before using the Java library you need a developer account and a signing key pair as explained [here](https://docs.truelayer.com/docs/sign-your-requests#step-1-generate-a-signing-key-pair).

### Initialize TrueLayerClient
```java
TrueLayerClient client = TrueLayerClient.New()
        .environment(Environment.sandbox()) // optional: to use TL sandbox environment
        .withHttpLogs() // optional: logs HTTP traces to stdout
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

More details about our HTTP logs can be found [here](https://github.com/TrueLayer/truelayer-java/wiki/HTTP-logging).

### Create a payment
```java
// build the payment request object
CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
        .amountInMinor(101)
        .currency(CurrencyCode.GBP)
        .paymentMethod(PaymentMethod.bankTransfer()
            .providerSelection(ProviderSelection.userSelected()
                    .filter(ProviderFilter.builder()
                    .countries(Collections.singletonList(CountryCode.GB))
                    .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                    .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                    .providerIds(Collections.singletonList("mock-payments-gb-redirect"))
                    .build())
                .build())
            .beneficiary(MerchantAccount.builder()
                .merchantAccountId("e83c4c20-b2ad-4b73-8a32-ee855362d72a")
                .build())
            .build())
        .user(User.builder()
            .name("Andrea")
            .email("andrea@truelayer.com")
            .build())
        .build();        

// fire the request        
CompletableFuture<ApiResponse<CreatePaymentResponse>> paymentResponse = client
    .payments()
    .createPayment(paymentRequest);

// wait for the response
ApiResponse<CreatePaymentResponse> payment = paymentResponse.get())
```

### Build a link to our hosted createPaymentResponse page
```java
URI hppLink = client.hpp().getHostedPaymentPageLink("your-createPaymentResponse-id",
        "your-createPaymentResponse-token",
        URI.create("http://yourdomain.com"));
```

## Building locally

To build the solution run:
```sh
./gradlew build
```

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

## Contributing

Contributions are always welcome!

Please adhere to this project's [code of conduct](CODE_OF_CONDUCT.md).
  
## License

[MIT](LICENSE)
