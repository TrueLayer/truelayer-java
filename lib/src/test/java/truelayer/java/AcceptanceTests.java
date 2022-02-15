package truelayer.java;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.Constants.HeaderNames.USER_AGENT;
import static truelayer.java.TestUtils.*;

import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;
import truelayer.java.entities.BankTransfer;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountResponse;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.StartAuthorizationFlowRequest.Redirect;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import truelayer.java.payments.entities.paymentmethod.Remitter;
import truelayer.java.payments.entities.paymentmethod.SortCodeAccountNumberAccountIdentifier;
import truelayer.java.payments.entities.paymentmethod.provider.PreselectedProviderSelection;
import truelayer.java.payments.entities.paymentmethod.provider.ProviderFilter;
import truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection;
import truelayer.java.payments.entities.paymentmethod.provider.UserSelectedProviderSelection;

@Tag("acceptance")
public class AcceptanceTests {
    public static final String LOCALHOST_RETURN_URI = "http://localhost:3000/callback";
    public static final String MOCK_PROVIDER_ID = "mock-payments-gb-redirect";

    private static TrueLayerClient tlClient;

    @BeforeAll
    public static void setup() {
        tlClient = TrueLayerClient.New()
                .environment(Environment.sandbox())
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

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of all merchant accounts associated to the client")
    public void shouldListAllMerchantAccounts() {
        ApiResponse<ListMerchantAccountResponse> response =
                tlClient.merchantAccounts().listMerchantAccounts().get();

        assertNotError(response);
    }

    @Test
    @DisplayName("It should create and get by id a payment with user_selected provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithUserSelectionProvider() {
        // create payment
        UserSelectedProviderSelection userSelectionProvider = UserSelectedProviderSelection.builder()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(MOCK_PROVIDER_ID))
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(userSelectionProvider);

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
    @SneakyThrows
    public void shouldCreateAPaymentWithPreselectedProvider() {
        // create payment
        PreselectedProviderSelection preselectionProvider = PreselectedProviderSelection.builder()
                .providerId(MOCK_PROVIDER_ID)
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .remitter(Remitter.builder()
                        .accountHolderName("Andrea Di Lisio")
                        .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(preselectionProvider);

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
                        createPaymentResponse.getData().getResourceToken(),
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
                .redirect(Redirect.builder()
                        .returnUri(URI.create(LOCALHOST_RETURN_URI))
                        .build())
                .withProviderSelection()
                .build();
        ApiResponse<StartAuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // Submit the provider selection
        ApiResponse<SubmitProviderSelectionResponse> submitProviderSelectionResponse = tlClient.payments()
                .submitProviderSelection(
                        createPaymentResponse.getData().getId(),
                        SubmitProviderSelectionRequest.builder()
                                .providerId(MOCK_PROVIDER_ID)
                                .build())
                .get();

        assertNotError(submitProviderSelectionResponse);
    }

    private CreatePaymentRequest buildPaymentRequestWithProviderSelection(ProviderSelection providerSelection) {
        return CreatePaymentRequest.builder()
                .amountInMinor(RandomUtils.nextInt(50, 500))
                .currency(CurrencyCode.GBP)
                .paymentMethod(BankTransfer.builder()
                        .providerSelection(providerSelection)
                        .beneficiary(MerchantAccount.builder()
                                .merchantAccountId("93e2c5f1-d935-47aa-90c0-be4da32738ee")
                                .build())
                        .build())
                .user(User.builder()
                        .name("Andrea Di Lisio")
                        .email("andrea@truelayer.com")
                        .build())
                .build();
    }

    private CreatePaymentRequest buildPaymentRequest() {
        UserSelectedProviderSelection userSelectionProvider = UserSelectedProviderSelection.builder()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(MOCK_PROVIDER_ID))
                        .build())
                .build();
        return buildPaymentRequestWithProviderSelection(userSelectionProvider);
    }
}
