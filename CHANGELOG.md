# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [17.4.0] - 2025-08-07
### Added
* Add support for `scheme_id` field in merchant account transaction responses for Payout and Refund transactions
* The `scheme_id` field provides information about the payment scheme used (e.g., "faster_payments_service", "sepa_credit_transfer", "internal_transfer")

## [17.3.0] - 2025-04-16
### Added
* Add sub_merchants support to Payments module

## [17.2.0] - 2025-04-16
### Added
* Add support for `deprecated_at` property to GET payment API response

## [17.1.0] - 2025-03-14
### Added
* Add support for PLN payouts

## [17.0.0] - 2025-01-15
### Changed
* ⚠️ Breaking: removed deprecated HPP link builder
* ⚠️ Breaking: Aligned custom cache implementation to other officially supported client libraries

## [16.3.1] - 2025-01-10
### Changed
* Upgrades linting and test libraries
* Upgrades quarkus-mvc test project and introduce CI workflow for that
* Upgrades CI environment
* Introduces CHANGELOG.md file 

### Changed
* feat(ACL-251): Adds method to generate SU+ Authentication URI for FI payments by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/332


## [16.3.0] - 2025-01-08

### Added
* feat(ACL-251): Adds method to generate SU+ Authentication URI for FI payments by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/332



## [16.2.0] - 2024-12-10

### Added
* Add search payments providers endpoint + statement reference to merchant account by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/330



## [16.1.0] - 2024-11-26

### Added
* [ACL-226] New HPP link builder with ability to show mandate results, and set wait and/or signup by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/328



## [16.0.1] - 2024-11-21

### Added
* chore(ACL-225): v3 endpoints used for Payments API by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/327



## [16.0.0] - 2024-10-30

### Added
* Merchant accounts updates by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/325



### Changes
- ⚠️ Breaking: removed `currency` property from `UpdateSweepingRequest` 

## [15.0.0] - 2024-10-18

### New features
- Signup+ endpoint to [generate the Auth URI to be used in Finland](https://docs.truelayer.com/reference/generateauthuri)
- Signup+ endpoint to [get identity data associated to a payment, both in UK and Finland](https://docs.truelayer.com/reference/getuserdatabypayment)
- Added the `Authorizing` status returned in the [Create Payment API response](https://docs.truelayer.com/reference/create-payment)
- Added the `scheme_selection` field in the [Create Payout API request](https://docs.truelayer.com/reference/create-payout)

### Changes
- ⚠️ Breaking: the currency property of the `PaymentDetail` class in now an enum `CurrencyCode`

## [14.3.0] - 2024-10-16

### Added
* [ACL-167] Add sub_merchants support to payouts by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/320



## [14.2.0] - 2024-09-27

### Added
* feat(ACL-195): Adds cancel payment support by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/315



## [14.1.0] - 2024-09-18

### Added
* [ACL-159] Add user_selected scheme selection by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/313



## [14.0.0] - 2024-09-17

### Added
* [ACL-180] Add preselected scheme selection to preselected provider by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/312
* ⚠️ Breaking ⚠️ : `SchemeSelection` property for `UserSelectedProviderSelection` object has been moved from `com.truelayer.java.payments.entities.schemeselection` package to `com.truelayer.java.payments.entities.schemeselection.userselected` package



## [13.3.0] - 2024-09-11

### Added
* [ACL-161] Add risk_assessment field to create payment request by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/311



## [13.2.0] - 2024-09-10

### Added
* feat(ACL-162): supports for verified payins by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/310



## [13.1.2] - 2024-08-05

### Added
* [ACL-174] Improve retry payments acceptance tests by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/303
* [ACL-175] Fix missing EqualsAndHashCode Lombok annotations by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/304



## [13.1.0] - 2024-07-25

### Added
* [ACL-32] Ownership change by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/299
* [ACL-138] Add support for retry parameter by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/300



## [13.0.5] - 2024-06-24

### Added
* [EWT-590] Fix mandates acceptance tests by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/296



## [13.0.4] - 2024-06-13

### Added
* [EWT-567] Version info management simplification by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/293



## [13.0.3] - 2024-05-27

### Added
* chore(EWT-561): improved reporting for acceptance tests by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/291



## [13.0.2] - 2024-05-17

### Added
* Adjust settlement/authorisation timeouts in acceptance tests, and review version info loader implementation by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/289



## [13.0.1] - 2024-05-16

### Added
* chore(EWT-560): Dependency updates and improved reporting for failed acceptance tests by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/288



## [13.0.0] - 2024-04-24

### Added
* fix(EWT-543): type change for description field in authorization flow actions objects by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/286



## [12.0.0] - 2024-04-17

### Added
* Added `capabilities.payments.bankTransfer.availability` field to `PaymentsProvider` response entity
* Added `capabilities.mandates.vrpSweeping.availability` field to `PaymentsProvider` response entity
* Added `capabilities.mandates.vrpSweeping.availability` field to `PaymentsProvider` response entity
* ⚠️ **BREAKING** ⚠️ Removed `errorRate` field from `ProviderAvailability` object (used in the new fields defined above and in the already existing `ProviderSelection` authorization flow action



## [11.1.0] - 2024-04-15

### Added
* feat(EWT-535): Support for merchant account transactions pagination by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/279



## [11.0.1] - 2024-02-13

### Added
* feat(PAYINS-504): Set Authorization header to `GET v3/payments-providers/{id}` by @tl-facundo-aita in https://github.com/TrueLayer/truelayer-java/pull/270



## [11.0.0] - 2024-01-08

### Added
* added constant for commercial VRP scope: RECURRING_PAYMENTS_COMMERCIAL
* ⚠️ Breaking ⚠️ : refactor the submit provider return response object to support multiple resource types (single payments and mandates)
* tests: refactor the way we do headless auth for payments and mandates in acceptance tests



## [10.3.1] - 2023-12-05

### Added
* chore(EWT-359): dependencies and CI updates by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/242



## [10.3.0] - 2023-11-03

### Added
* [EWT-371] Add payment_source property to authorized and failed payment details by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/234



## [10.2.0] - 2023-10-18

### Added
* [DNTT-489] Added settlement_risk to executed and settled payment details response types by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/233



## [10.1.1] - 2023-10-12

### Added
* chore(EWT-358): upgrade to Wiremock 3.X, and more internal deps by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/230



## [10.1.0] - 2023-10-12

### Added
* Adds support for `related_products` object on payment and mandate creation requests
* Adds support for `X-Device-User-Agent` HTTP header on start authorization flow

PR: https://github.com/TrueLayer/truelayer-java/pull/228


## [10.0.0] - 2023-09-04

### Added
- **new**: feature to set explicit global OAuth scopes globally on the client. More on [this Wiki entry](https://github.com/TrueLayer/truelayer-java/wiki/Oauth-access-tokens-management#global-oauth-scopes)
- **changed:** Adapts the /token/connect request to the API specs
-  **changed:** Sets explicit scopes on all authenticated requests (relates to #210)
-  **changed ⚠️ BREAKING** : changes how caching works and the ICredentialsCache to work with scopes. More on this [wiki page](https://github.com/TrueLayer/truelayer-java/wiki/Oauth-access-tokens-management)


## [9.0.1] - 2023-08-24

### Added
* [Dependabot]: Bump org.junit.jupiter:junit-jupiter from 5.9.3 to 5.10.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/209
* [Dependabot]: Bump com.diffplug.spotless from 6.19.0 to 6.20.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/207
* [Dependabot]: Bump io.freefair.lombok from 8.1.0 to 8.2.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/212
* [EWT-278] Build also against JDK 20 by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/211
* [Dependabot]: Bump io.freefair.lombok from 8.2.1 to 8.2.2 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/214
* fix(EWT-328): improperly formatted datetime string on transactions endpoint by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/216



## [9.0.0] - 2023-07-19

### Added
* [Dependabot]: Bump io.freefair.lombok from 8.0.1 to 8.1.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/206
* [EWT-306] Fix models inconsistencies by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/208

## Breaking change notice
* on `Failed` mandates object, renamed `authorisationFailedAt` property to `failedAt`
* on `Revoked` mandates object, removed `remitter` property



## [8.0.0] - 2023-05-24

### Added
* [Dependabot]: Bump org.apache.commons:commons-configuration2 from 2.8.0 to 2.9.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/196
* [Dependabot]: Bump com.squareup.okhttp3:okhttp from 4.10.0 to 4.11.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/199
* [Dependabot]: Bump org.junit.jupiter:junit-jupiter from 5.9.2 to 5.9.3 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/200
* [Dependabot]: Bump com.diffplug.spotless from 6.17.0 to 6.18.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/197
* [EWT-192] Add support for a limited set of custom HTTP headers by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/204

## Breaking change notice
- The ITrueLayerClient interface returns only _Handlers_ interface, as opposed to the previous mix of _Api_ and _Handlers_ interface.
- the old mechanism to set the `X-Forwarded-For` header on the `startAuthorizationFlow` invocations is superseded by the new API. 
- The (empty) SubmitConsentRequest object has been removed in favour of a more generic empty request body type.



## [7.0.0] - 2023-05-11

## Breaking change notice
`Beneficiary` entity and its extensions (`ExternalAccount`, `PaymentSource`, `BusinessAccount`) have been moved from `com.truelayer.java.merchantaccounts.entities.transactions.beneficiary` to `com.truelayer.java.entities.beneficiary` package. The same entities are referenced from the newly introduced payout entities

### Added
* [EWT-194] Add payouts support by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/203



## [6.3.0] - 2023-05-04

### Added
* [EWT-191] Add support for custom proxy configurations with authentication by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/202



## [6.2.0] - 2023-05-03

### Added
* [EWT-78] Add payment refunds support by @tl-luca-baggi in https://github.com/TrueLayer/truelayer-java/pull/201



## [6.1.0] - 2023-03-27

### Added
* [Dependabot]: Bump io.github.gradle-nexus.publish-plugin from 1.2.0 to 1.3.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/190
* [Dependabot]: Bump com.diffplug.spotless from 6.16.0 to 6.17.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/192
* [EWT-135] feat: adds support for `X-Forwarded-For` HTTP header in start auth flow for single payments by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/195



## [6.0.1] - 2023-03-06

### Added
* [Dependabot]: Bump junit-jupiter from 5.9.1 to 5.9.2 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/179
* [EWT-99] bump com.diffplug.spotless from 6.12.1 to 6.14.1 + Removes java 8 from our CI matrix by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/183
* [EWT-73] Updates nexus publish-plugin, gradle wrapper and other internal libraries by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/189



## [6.0.0] - 2023-01-13

### Added
* [EWT-85] fixes existing items returned by the `merchant-accounts/{id}/transactions` endpoint and add support for refunds by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/180



## [5.0.0] - 2023-01-11

### Added
* [Dependabot]: Bump com.diffplug.spotless from 6.12.0 to 6.12.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/177
* feat(PAYG-1374): Remove user info from get payment and mandate response by @tl-tai-tang in https://github.com/TrueLayer/truelayer-java/pull/178

## New Contributors
* @tl-tai-tang made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/178


## [4.2.0] - 2022-12-22

### Added
* [Dependabot]: Bump io.freefair.lombok from 6.6 to 6.6.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/174
* feat: [PAYG-1363] add PLN and NOK currencies by @Lindronics in https://github.com/TrueLayer/truelayer-java/pull/176



## [4.1.0] - 2022-12-20

### Added
* Github actions updates by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/173
* [EWT-79] Default READ timeout reviewed + bugfix for existing custom timeout logic by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/175



## [4.0.2] - 2022-12-16

### Added
* transitive dependencies constraint block to replace previous direct dependencies by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/172



## [4.0.1] - 2022-12-16

### Added
* Bumps dependencies to solve a few vulnerabitities by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/171



## [4.0.0] - 2022-12-15

### Added
* [EWT-63] `scheme_selection` support for single payments by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/170
* [REC-656] Async and Smart payment retries for recurring payments
* AuthorizationFlow related deprecated objects removal and provider selection DTOs renamed (leftovers)



## [3.2.0] - 2022-12-13

### Added
* [REC-740] Get Mandate Constraints + tests by @tl-andrei-sorbun in https://github.com/TrueLayer/truelayer-java/pull/169



## [3.1.1] - 2022-12-05

### Added
* [JSDK-58] Captures Java version in TL-Agent, upgrades Gradle wrapper and CI by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/167



## [3.1.0] - 2022-12-05

### Added
* [JSDK-86] New getters to help with responses interpretations + Dependency updates by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/166



## [3.0.1] - 2022-12-01

### Added
* [EWT-61] feat(observability): User-agent HTTP header replaced with TL-Agent by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/164



## [3.0.0] - 2022-11-30

## Breaking change notice
The `Mandate` and `MandateDetail` types now reference a `Beneficiary` class included in package `com.truelayer.java.mandates.entities.beneficiary` as opposed to the previous `com.truelayer.java.payments.entities.beneficiary`. Moreover, the new `Beneficiary` type for mandates does not contain any a reference field in it. This has now been moved into the `Mandate` type itself.

### Added
* [Dependabot]: Bump com.diffplug.spotless from 6.11.0 to 6.12.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/163
* [REC-725] feat(mandate reference): optional mandate reference added by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/162



## [2.6.0] - 2022-11-17

### Added
* [Dependabot]: Bump wiremock-jre8 from 2.34.0 to 2.35.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/158
* [REC-718] feat(recurring payments): reference added to create payment request for mandates by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/161



## [2.5.1] - 2022-11-08

### Added
* fix: `form` and `consent` action response deserialisation by @Lindronics in https://github.com/TrueLayer/truelayer-java/pull/160



## [2.5.0] - 2022-10-10

### Added
* feat: [JSDK-70] get payments provider endpoint by @Lindronics in https://github.com/TrueLayer/truelayer-java/pull/156
* fix: upgraded version to 2.5.0 by @Lindronics in https://github.com/TrueLayer/truelayer-java/pull/157



## [2.4.0] - 2022-10-04

### Added
* feat: [PAYG-1169] add status to create payment response by @Lindronics in https://github.com/TrueLayer/truelayer-java/pull/155



## [2.3.0] - 2022-09-28

### Added
* [Dependabot]: Bump junit-jupiter from 5.9.0 to 5.9.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/153
* feat: [PAYG-1148] Add consent and form action support by @Lindronics in https://github.com/TrueLayer/truelayer-java/pull/154

## New Contributors
* @Lindronics made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/154


## [2.2.3] - 2022-09-20

### Added
* dependencies updates by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/152



## [2.2.2] - 2022-09-14

### Added
* [PAYG-1133] Add extra fields to user object by @tl-wajid-malik in https://github.com/TrueLayer/truelayer-java/pull/148

## New Contributors
* @tl-wajid-malik made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/148


## [2.2.1] - 2022-09-14

### Added
* [REC-631] reference added to payments into merchant accounts by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/149



## [2.2.0] - 2022-08-29

### Added
* [JSDK-80] Confirmation of Funds by @tl-andrei-sorbun in https://github.com/TrueLayer/truelayer-java/pull/144



## [2.1.1] - 2022-08-24

### Added
* Bump signing library by @tl-andrei-sorbun in https://github.com/TrueLayer/truelayer-java/pull/143



## [2.1.0] - 2022-08-24

### Added
* [Dependabot]: Bump com.diffplug.spotless from 6.9.0 to 6.9.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/139
* [Dependabot]: Bump com.diffplug.spotless from 6.9.1 to 6.10.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/142
* [REC-550] Update SDK with new Remitter field by @tl-andrei-sorbun in https://github.com/TrueLayer/truelayer-java/pull/141

## New Contributors
* @tl-andrei-sorbun made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/141


## [2.0.4] - 2022-08-23

### Added
* [PAYG-920] remove auth flow from authorizing by @tl-facundo-aita in https://github.com/TrueLayer/truelayer-java/pull/133



## [2.0.3] - 2022-08-04

### Added
* [Dependabot]: Bump com.diffplug.spotless from 6.5.2 to 6.9.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/134
* [Dependabot]: Bump jackson-databind from 2.13.2.2 to 2.13.3 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/117
* [Dependabot]: Bump junit-jupiter from 5.8.2 to 5.9.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/138
* [Dependabot]: Bump truelayer-signing from 0.2.0 to 0.2.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/128
* [Dependabot]: Bump commons-configuration2 from 2.7 to 2.8.0 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/129
* [Dependabot]: Bump io.freefair.lombok from 6.4.3 to 6.5.0.3 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/130
* [REC-567] `provider_selection` object on mandate detail DTO by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/137



## [2.0.2] - 2022-08-03

### Added
* [JSDK-82] Javadoc simplification by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/131
* [JSDK-83] Sample app documentation improvement by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/135
* [JSDK-64] `metadata` object on payments and mandates DTOs by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/136



## [2.0.1] - 2022-07-18

### Added
* [JSDK-77] Client builder refactor for testability by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/125



## [2.0.0] - 2022-06-21

### Added

### New features
- Onboards Recurring payments functionalities as per our public docs.
-  Adds support for settlement_risk in GET payment by id responses

### Breaking changes
- `PaymentAuthorizationFlowResponse` renamed to `AuthorizationFlowResponse`, used in both payments and mandates
-  Removed `metadata` in `AuthorizationFlowResponse` of type `redirect`
-  renamed package for `Provider` class and `providerId` field renamed to `id` in ProviderSelction in authorization flow responses
- `getTransactions` method and `GetTransactionsResponse` renamed to `listTransactions` and `ListTransactionsResonse` respectively. method params simplified with the help of new `ListTransactionsQuery` type
- `getPaymentSources` and `ListPaymentSourcesResponse` renamed to `listPaymentSources` and `ListPaymentSourcesResponse` respectively. method params simplified with the help of new `ListPaymentSourcesQuery` type
- Java 8 `ZonedDateTime` to `replace java.util.Date`

## New Contributors
* @nico-incubiq made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/121


## [1.1.0] - 2022-05-09

### Added
* [JSDK-74] Improved configurability: call timeout, connection pool and request dispatcher by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/115



## [1.0.0] - 2022-04-13

### Added
* [JSDK-60] Support for provider return params by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/96



## [0.7.5] - 2022-04-06

### Added
* [JSDK-66] Bugfix serialization issue by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/103



## [0.7.4] - 2022-04-05

### Added
* [JSDK-63] response DTOs usability improvements by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/100



## [0.7.3] - 2022-04-05

### Added
* Dependabot and CVE-2020-36518 resolution by @dili91 in https://github.com/TrueLayer/truelayer-java/pull/101

## New Contributors
* @dili91 made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/101


## [0.7.2] - 2022-04-01

### Added
* [JSDK-62] Provider id and scheme id added in GET payment response by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/99



## [0.7.1] - 2022-04-01

### Added
* [JSDK-61] Adds support for unknown properties on response DTOs by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/98



## [0.7.0] - 2022-03-18

### Added
* [JSDK-57] Optional credentials caching + custom pluggable cache by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/94



## [0.6.0] - 2022-03-15

### Added
* [JSDK-55] Custom logging implemented by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/92



## [0.5.0] - 2022-03-14

### Added
* [JSDK-53] Contributing by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/88
* [JSDK-36] Access token cache by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/83



## [0.4.14] - 2022-03-11

### Added
* [Dependabot]: Bump io.freefair.lombok from 6.2.0 to 6.4.1 by @dependabot in https://github.com/TrueLayer/truelayer-java/pull/86



## [0.4.13] - 2022-03-08

### Added
* [JSDK-50] Update tl signing version by @azanin in https://github.com/TrueLayer/truelayer-java/pull/82

## New Contributors
* @azanin made their first contribution in https://github.com/TrueLayer/truelayer-java/pull/82


## [0.4.12] - 2022-03-07

### Added
* [JSDK-48] Javadoc and doc improvements by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/81



## [0.4.11] - 2022-03-04

### Added
* [JSDK-9] Public releases and CI changes by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/79
* [JSDK-9] CI Workflow fixed by @tl-andrea-dilisio in https://github.com/TrueLayer/truelayer-java/pull/80



## [0.4.10] - 2022-03-01


## [0.4.9] - 2022-03-01









## [0.4.7] - 2022-02-18











## [0.4.6] - 2022-02-15









## [0.4.4] - 2022-02-09











## [0.4.3] - 2022-02-04



## [0.4.2] - 2022-02-04



## [0.4.1] - 2022-02-03



## [0.4.0] - 2022-02-03



## [0.3.9] - 2022-02-03





## [0.3.8] - 2022-02-02





## [0.3.7] - 2022-02-01





## [0.3.6] - 2022-01-27



## [0.3.5] - 2022-01-26



## [0.3.4] - 2022-01-25



## [0.3.3] - 2022-01-25















## [0.3.1] - 2022-01-24











## [0.3.0] - 2022-01-21



















## [0.1.7] - 2022-01-18



## [0.1.6] - 2022-01-18



## [0.1.5] - 2022-01-18



## [0.1.4] - 2022-01-17











## [0.1.1] - 2022-01-17









## [0.1.0] - 2022-01-12





## [0.0.7] - 2022-01-11





## [0.0.5] - 2022-01-10



## [0.0.4] - 2022-01-10





























## [0.0.1] - 2022-01-04



