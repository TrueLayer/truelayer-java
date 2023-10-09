package com.truelayer.java.commonapi;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.TestUtils;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.http.entities.Headers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CommonHandlerTests {

    @Test
    @DisplayName("It should call the submit payment return parameters endpoint")
    public void shouldCallSubmitPaymentReturnParamsEndpoint() {
        ICommonApi commonApi = Mockito.mock(ICommonApi.class);
        CommonHandler sut = new CommonHandler(commonApi);
        SubmitPaymentReturnParametersRequest request = SubmitPaymentReturnParametersRequest.builder()
                .query("a-query")
                .fragment("a-fragment")
                .build();

        sut.submitPaymentReturnParameters(request);

        verify(commonApi, times(1)).submitPaymentReturnParameters(emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the submit payment return parameters endpoint with additional headers")
    public void shouldCallSubmitPaymentReturnParamsEndpointWithCustomHeaders() {
        ICommonApi commonApi = Mockito.mock(ICommonApi.class);
        CommonHandler sut = new CommonHandler(commonApi);
        Headers customHeaders = TestUtils.buildTestHeaders();
        SubmitPaymentReturnParametersRequest request = SubmitPaymentReturnParametersRequest.builder()
                .query("a-query")
                .fragment("a-fragment")
                .build();

        sut.submitPaymentReturnParameters(customHeaders, request);

        verify(commonApi, times(1)).submitPaymentReturnParameters(toMap(customHeaders), request);
    }
}
