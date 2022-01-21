package truelayer.java;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.TestUtils.assertNotError;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentmethod.BankTransfer;
import truelayer.java.payments.entities.paymentmethod.Remitter;
import truelayer.java.payments.entities.paymentmethod.SortCodeAccountNumberSchemeIdentifier;
import truelayer.java.payments.entities.paymentmethod.provider.BaseProvider;
import truelayer.java.payments.entities.paymentmethod.provider.PreselectedProvider;
import truelayer.java.payments.entities.paymentmethod.provider.ProviderFilter;
import truelayer.java.payments.entities.paymentmethod.provider.UserSelectionProvider;

@Tag("acceptance")
public class AcceptanceTests {
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
        var userSelectionProvider = UserSelectionProvider.builder()
                .filter(ProviderFilter.builder()
                        .countries(List.of(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(List.of(CustomerSegment.RETAIL))
                        .providerIds(List.of("mock-payments-gb-redirect"))
                        .excludes(ProviderFilter.Excludes.builder().build())
                        .build())
                .build();
        var paymentRequest = buildPaymentRequestWithProvider(userSelectionProvider);

        var createPaymentResponse = tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // get it by id
        var getPaymentByIdResponse =
                tlClient.payments().getPayment(createPaymentResponse.getData().getId()).get();

        assertNotError(getPaymentByIdResponse);
    }

    @Test
    @DisplayName("It should create and get by id a payment with preselected provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithPreselectedProvider() {
        // create payment
        var preselectedProvider = PreselectedProvider.builder()
                .providerId("mock-payments-gb-redirect")
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .remitter(Remitter.builder()
                        .name("Andrea Di Lisio")
                        .schemeIdentifier(SortCodeAccountNumberSchemeIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
        var paymentRequest = buildPaymentRequestWithProvider(preselectedProvider);

        var createPaymentResponse = tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // get it by id
        var getPaymentByIdResponse =
                tlClient.payments().getPayment(createPaymentResponse.getData().getId()).get();

        assertNotError(getPaymentByIdResponse);
    }

    @Test
    @DisplayName("It should create a payment and open it in TL HPP")
    @SneakyThrows
    public void shouldCreateAPaymentAndOpenItInHPP() {
        // create payment
        var paymentRequest = buildPaymentRequest();

        var createPaymentResponse = tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // open it in HPP
        var hppPageRequest = HttpRequest.newBuilder(tlClient.hpp()
                        .getHostedPaymentPageLink(
                                createPaymentResponse.getData().getId(),
                                createPaymentResponse.getData().getPaymentToken(),
                                URI.create("http://localhost")))
                .GET()
                .build();

        var hppResponse = getHttpClient().send(hppPageRequest, HttpResponse.BodyHandlers.ofString());
        assertTrue(hppResponse.statusCode() >= 200 && hppResponse.statusCode() < 300);
    }

    private CreatePaymentRequest buildPaymentRequestWithProvider(BaseProvider baseProvider) {
        return CreatePaymentRequest.builder()
                .amountInMinor(RandomUtils.nextInt(50, 500))
                .currency("GBP")
                .paymentMethod(BankTransfer.builder().provider(baseProvider).build())
                .beneficiary(MerchantAccount.builder()
                        .id("e83c4c20-b2ad-4b73-8a32-ee855362d72a")
                        .build())
                .user(CreatePaymentRequest.User.builder()
                        .name("Andrea Di Lisio")
                        .type(CreatePaymentRequest.User.Type.NEW)
                        .email("andrea@truelayer.com")
                        .build())
                .build();
    }

    private CreatePaymentRequest buildPaymentRequest() {
        var userSelectionProvider = UserSelectionProvider.builder()
                .filter(ProviderFilter.builder()
                        .countries(List.of(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(List.of(CustomerSegment.RETAIL))
                        .providerIds(List.of("mock-payments-gb-redirect"))
                        // todo review
                        .excludes(ProviderFilter.Excludes.builder().build())
                        .build())
                .build();
        return buildPaymentRequestWithProvider(userSelectionProvider);
    }

    private HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }
}
