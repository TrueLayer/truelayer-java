package com.truelayer.java.payouts;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PayoutsHandlerTests {

    private static final String A_PAYOUT_ID = "a-payout-id";
    private static final RequestScopes SCOPES =
            RequestScopes.builder().scope(Constants.Scopes.PAYMENTS).build();

    @Test
    @DisplayName("It should call the create payout endpoint with empty headers map")
    public void shouldCallCreatePayoutEndpoint() {
        IPayoutsApi payoutsApi = Mockito.mock(IPayoutsApi.class);
        PayoutsHandler sut = new PayoutsHandler(payoutsApi);
        CreatePayoutRequest request =
                CreatePayoutRequest.builder().amountInMinor(100).build();

        sut.createPayout(request);

        verify(payoutsApi, times(1)).createPayout(SCOPES, emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the create payout endpoint with custom headers")
    public void shouldCallCreatePayoutEndpointWithCustomHeaders() {
        IPayoutsApi payoutsApi = Mockito.mock(IPayoutsApi.class);
        PayoutsHandler sut = new PayoutsHandler(payoutsApi);
        Headers customHeaders = TestUtils.buildTestHeaders();
        CreatePayoutRequest request =
                CreatePayoutRequest.builder().amountInMinor(100).build();

        sut.createPayout(customHeaders, request);

        verify(payoutsApi, times(1)).createPayout(SCOPES, toMap(customHeaders), request);
    }

    @Test
    @DisplayName("It should call the get payout by id endpoint")
    public void shouldCallGetPayoutByIdEndpoint() {
        IPayoutsApi payoutsApi = Mockito.mock(IPayoutsApi.class);
        PayoutsHandler sut = new PayoutsHandler(payoutsApi);

        sut.getPayout(A_PAYOUT_ID);

        verify(payoutsApi, times(1)).getPayout(SCOPES, A_PAYOUT_ID);
    }
}
