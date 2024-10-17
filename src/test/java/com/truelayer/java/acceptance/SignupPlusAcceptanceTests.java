package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.TestUtils;
import com.truelayer.java.entities.*;
import com.truelayer.java.entities.accountidentifier.SortCodeAccountNumberAccountIdentifier;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import com.truelayer.java.payments.entities.schemeselection.preselected.SchemeSelection;
import com.truelayer.java.signupplus.entities.UserData;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("acceptance")
public class SignupPlusAcceptanceTests extends AcceptanceTests {

    public static final URI RETURN_URI = URI.create("http://localhost:3000/callback");
    public static final String PROVIDER_ID = "mock-payments-gb-redirect";

    @DisplayName("It should create a payment and get the associated identity data via Signup+")
    @ParameterizedTest(name = "with provider={0} and currency={1}")
    @CsvSource({"mock-payments-gb-redirect, GBP"})
    @SneakyThrows
    public void itShouldAuthorizeAUkPaymentAndThenQueryTheAssociatedIdentityData(
            String providerId, String currencyCodeString) {
        // Create and authorize a payment
        String paymentId = createAndAuthorizePayment(
                "mock-payments-gb-redirect", CurrencyCode.valueOf(currencyCodeString), RETURN_URI);
        // wait for its settlement
        waitForPaymentStatusUpdate(tlClient, paymentId, Status.SETTLED);

        // get the identity data associated with that
        ApiResponse<UserData> userDataResponse =
                tlClient.signupPlus().getUserDataByPayment(paymentId).get();
        assertNotError(userDataResponse);
    }

    @SneakyThrows
    private String createAndAuthorizePayment(String providerId, CurrencyCode currencyCode, URI returnUri) {
        MerchantAccount account = getMerchantAccount(currencyCode);

        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(ThreadLocalRandom.current().nextInt(50, 500))
                .currency(currencyCode)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(ProviderSelection.preselected()
                                .providerId(providerId)
                                .schemeSelection(
                                        SchemeSelection.instantPreferred().build())
                                .remitter(Remitter.builder()
                                        .accountHolderName("Andrea Di Lisio")
                                        .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                                .accountNumber("12345678")
                                                .sortCode("123456")
                                                .build())
                                        .build())
                                .build())
                        .beneficiary(Beneficiary.merchantAccount()
                                .merchantAccountId(account.getId())
                                .reference(UUID.randomUUID().toString())
                                .build())
                        .build())
                .user(User.builder()
                        .name("Andrea Di Lisio")
                        .email("andrea@truelayer.com")
                        .dateOfBirth(LocalDate.now())
                        .address(Address.builder()
                                .addressLine1("1 Hardwick Street")
                                .city("London")
                                .state("Greater London")
                                .zip("EC1R 4RB")
                                .countryCode("GB")
                                .build())
                        .build())
                .relatedProducts(
                        RelatedProducts.builder().signupPlus(new HashMap<>()).build())
                .build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        String paymentId = createPaymentResponse.getData().getId();

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(returnUri)
                        .directReturnUri(returnUri)
                        .build())
                .withProviderSelection()
                .build();
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(paymentId, startAuthorizationFlowRequest)
                .get();
        assertNotError(startAuthorizationFlowResponse);

        URI redirectUri = startAuthorizationFlowResponse
                .getData()
                .asAuthorizing()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();
        runAndAssertHeadlessResourceAuthorisation(
                tlClient,
                redirectUri,
                TestUtils.HeadlessResourceAuthorization.builder()
                        .action(TestUtils.HeadlessResourceAction.EXECUTE)
                        .resource(TestUtils.HeadlessResource.PAYMENTS)
                        .build());

        return createPaymentResponse.getData().getId();
    }
}
