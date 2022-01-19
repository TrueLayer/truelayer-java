package truelayer.java.http.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static truelayer.java.TestUtils.*;
import static truelayer.java.common.Utils.getObjectMapper;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;
import truelayer.java.TestUtils;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentResponse;

class ApiCallTest {

    @SneakyThrows
    @Test
    @DisplayName("It should return a successful response with a data object")
    public void itShouldReturnASuccessfulResponseWithData() {
        var payment = TestUtils.buildCreatePaymentResponse();
        var mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(Response.success(payment));
        var sut = new ApiCall(mockCall);

        var response = (ApiResponse<CreatePaymentResponse>) sut.execute().body();

        assertNotError(response);
        assertEquals(payment.getPaymentToken(), response.getData().getPaymentToken());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should return an error response in API returns an error")
    public void itShouldReturnAnErrorResponse() {
        var error = buildError();
        var mockCall = mock(Call.class);
        when(mockCall.execute())
                .thenReturn(Response.error(
                        400,
                        ResponseBody.create(
                                getObjectMapper().writeValueAsBytes(error), MediaType.get("application/json"))));
        var sut = new ApiCall(mockCall);

        var response = (ApiResponse<CreatePaymentResponse>) sut.execute().body();

        assertTrue(response.isError());
        assertEquals(error.getTitle(), response.getError().getTitle());
    }
}
