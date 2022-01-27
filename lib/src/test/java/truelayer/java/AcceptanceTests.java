package truelayer.java;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.TestUtils.*;
import static truelayer.java.common.Constants.HeaderNames.USER_AGENT;

import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.StartAuthorizationFlowRequest.Redirect;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import truelayer.java.payments.entities.paymentmethod.BankTransfer;
import truelayer.java.payments.entities.paymentmethod.Remitter;
import truelayer.java.payments.entities.paymentmethod.SortCodeAccountNumberSchemeIdentifier;
import truelayer.java.payments.entities.paymentmethod.provider.PreselectionProvider;
import truelayer.java.payments.entities.paymentmethod.provider.Provider;
import truelayer.java.payments.entities.paymentmethod.provider.ProviderFilter;
import truelayer.java.payments.entities.paymentmethod.provider.UserSelectionProvider;

@Tag("acceptance")
public class AcceptanceTests {
    public static final String LOCALHOST_RETURN_URI = "http://localhost:8080";
    public static final String MOCK_PROVIDER_ID = "mock-payments-gb-redirect";

    private static TrueLayerClient tlClient;

    @BeforeAll
    public static void setup() {
        tlClient = TrueLayerClient.New()
                .useSandbox()
                .clientCredentials(ClientCredentials.builder()
                        .clientId(System.getenv("TL_CLIENT_ID"))
                        .clientSecret(System.getenv("TL_CLIENT_SECRET"))
                        .build())
                .signingOptions(SigningOptions.builder()
                        .keyId(System.getenv("TL_SIGNING_KEY_ID"))
                        .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY").getBytes(StandardCharsets.UTF_8))
                        .build())
                .build();
    }

    @Test
    @DisplayName("It should create and get by id a payment with user_selection provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithUserSelectionProvider() {
        // create payment
        UserSelectionProvider userSelectionProvider = UserSelectionProvider.builder()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(MOCK_PROVIDER_ID))
                        .excludes(ProviderFilter.Excludes.builder().build())
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProvider(userSelectionProvider);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // get it by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);
    }

    @Test
    @DisplayName("It should create and get by id a payment with preselected provider")
    @Disabled
    @SneakyThrows
    public void shouldCreateAPaymentWithPreselectedProvider() {
        // create payment
        PreselectionProvider preselectionProvider = PreselectionProvider.builder()
                .providerId(MOCK_PROVIDER_ID)
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .remitter(Remitter.builder()
                        .name("Andrea Di Lisio")
                        .schemeIdentifier(SortCodeAccountNumberSchemeIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProvider(preselectionProvider);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // get it by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);
    }

    @Test
    @DisplayName("It should create a payment and open it in TL HPP")
    @SneakyThrows
    public void shouldCreateAPaymentAndOpenItInHPP() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequest();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // open it in HPP
        URI link = tlClient.hpp()
                .getHostedPaymentPageLink(
                        createPaymentResponse.getData().getId(),
                        createPaymentResponse.getData().getPaymentToken(),
                        URI.create(LOCALHOST_RETURN_URI));
        HttpURLConnection connection = (HttpURLConnection) link.toURL().openConnection();
        connection.setRequestProperty(USER_AGENT, LIBRARY_NAME + "/" + LIBRARY_VERSION);
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        assertTrue(responseCode >= 200 && responseCode < 300);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should complete an authorization flow for a payment")
    public void shouldCompleteAnAuthorizationFlowForAPayment() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequest();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder().returnUri(LOCALHOST_RETURN_URI).build())
                .withProviderSelection()
                .build();
        ApiResponse<StartAuthorizationFlowResponse> startAuthorizationFlowResponseResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponseResponse);

        // Submit the provider selection
        ApiResponse<SubmitProviderSelectionResponse> submitProviderSelectionResponseResponse = tlClient.payments()
                .submitProviderSelection(
                        createPaymentResponse.getData().getId(),
                        SubmitProviderSelectionRequest.builder()
                                .providerId(MOCK_PROVIDER_ID)
                                .build())
                .get();

        assertNotError(submitProviderSelectionResponseResponse);
    }

    private CreatePaymentRequest buildPaymentRequestWithProvider(Provider provider) {
        return CreatePaymentRequest.builder()
                .amountInMinor(RandomUtils.nextInt(50, 500))
                .currency("GBP")
                .paymentMethod(BankTransfer.builder().provider(provider).build())
                .beneficiary(MerchantAccount.builder()
                        .id("e83c4c20-b2ad-4b73-8a32-ee855362d72a")
                        .build())
                .user(User.builder()
                        .name("Andrea Di Lisio")
                        .email("andrea@truelayer.com")
                        .build())
                .build();
    }

    private CreatePaymentRequest buildPaymentRequest() {
        UserSelectionProvider userSelectionProvider = UserSelectionProvider.builder()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(MOCK_PROVIDER_ID))
                        .build())
                .build();
        return buildPaymentRequestWithProvider(userSelectionProvider);
    }
}
