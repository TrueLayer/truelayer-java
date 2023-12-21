package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("acceptance")
public class PaymentsProvidersAcceptanceTests extends AcceptanceTests {

    public static final String PROVIDER_ID = "mock-payments-gb-redirect";

    @Test
    //    @Disabled(
    //            "Temporarily disabled because the mock is released in alpha, but the release channel enum deliberately
    // doesn't have alpha")
    @DisplayName("It should get by id a payments provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithUserSelectionProvider() {
        ApiResponse<PaymentsProvider> getPaymentsProviderResponse =
                tlClient.paymentsProviders().getProvider(PROVIDER_ID).get();

        assertNotError(getPaymentsProviderResponse);
        assertEquals(getPaymentsProviderResponse.getData().getId(), PROVIDER_ID);
    }
}
