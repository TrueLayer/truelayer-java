package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.CountryCode;
import com.truelayer.java.payments.entities.CustomerSegment;
import com.truelayer.java.payments.entities.ReleaseChannel;
import com.truelayer.java.paymentsproviders.entities.*;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("acceptance")
public class PaymentsProvidersAcceptanceTests extends AcceptanceTests {

    public static final String PROVIDER_ID = "mock-payments-gb-redirect";

    @Test
    @DisplayName("It should get a payments provider by id")
    @SneakyThrows
    public void shouldGetAPaymentsProviderById() {
        ApiResponse<PaymentsProvider> getPaymentsProviderResponse =
                tlClient.paymentsProviders().getProvider(PROVIDER_ID).get();

        assertNotError(getPaymentsProviderResponse);
        assertEquals(getPaymentsProviderResponse.getData().getId(), PROVIDER_ID);
    }

    @Test
    @DisplayName("It should search for payments providers")
    @SneakyThrows
    public void shouldSearchPaymentsProviders() {
        SearchPaymentProvidersRequest request = SearchPaymentProvidersRequest.builder()
                .countries(List.of(CountryCode.GB))
                .currencies(List.of(CurrencyCode.GBP))
                .releaseChannel(ReleaseChannel.PRIVATE_BETA)
                .capabilities(Capabilities.builder()
                        .payments(PaymentsCapabilities.builder()
                                .bankTransfer(BankTransferCapabilities.builder().build())
                                .build())
                        .build())
                .customerSegments(List.of(CustomerSegment.RETAIL))
                .authorizationFlow(AuthorizationFlow.builder()
                        .configuration(Configuration.builder()
                                .redirect(Redirect.builder().build())
                                .build())
                        .build())
                .build();

        ApiResponse<SearchPaymentProvidersResponse> searchPaymentsProvidersResponse =
                tlClient.paymentsProviders().searchProviders(request).get();

        assertNotError(searchPaymentsProvidersResponse);
    }
}
