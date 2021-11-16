# TrueLayer Java

[![License](https://img.shields.io/:license-mit-blue.svg)](https://truelayer.mit-license.org/)


The official [TrueLayer](https://truelayer.com) Java client provides convenient access to TrueLayer APIs from applications built with Java. 

The library currently targets Java 11.

## Installation


### Pre-release Packages


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
```
var client = TrueLayerClient.builder()
    .clientId(A_CLIENT_ID)
    .clientSecret(A_CLIENT_SECRET)
    .signingOptions(SigningOptions.builder()
        .keyId("my-key-id")
        .privateKey("my-private-key-content") //temporary
        .build())
    .build();
```

### Make a payment
```
var paymentRequest = CreatePaymentRequest.builder()
    .amountInMinor(100)
    .currency(CreatePaymentRequest.Currency.GBP)
    .paymentMethod(PaymentMethod.builder()
            .statement_reference("a-payment-ref")
            .type(BANK_TRANSFER)
            .build())
    .build();

var payment = client
    .payments()
    .createPayment(paymentRequest);
```

## Building locally

## Testing
### Unit tests

To run unit tests: 
```
./gradlew unit-tests
```

### Integration tests

To run integration tests:
```
./gradlew integration-tests
```

## Library Documentation

## Contributing

Contributions are always welcome!

See [contributing](contributing.md) for ways to get started.

Please adhere to this project's [code of conduct](CODE_OF_CONDUCT.md).

  
## License

[MIT](LICENSE)
