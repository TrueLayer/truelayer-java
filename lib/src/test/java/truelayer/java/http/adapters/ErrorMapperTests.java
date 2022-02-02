package truelayer.java.http.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static truelayer.java.TestUtils.JSON_RESPONSES_LOCATION;
import static truelayer.java.TestUtils.deserializeJsonFileTo;
import static truelayer.java.common.Constants.HeaderNames.TL_CORRELATION_ID;
import static truelayer.java.http.adapters.ErrorMapper.GENERIC_ERROR_TITLE;
import static truelayer.java.http.adapters.ErrorMapper.GENERIC_ERROR_TYPE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import retrofit2.Response;
import truelayer.java.http.entities.ProblemDetails;

class ErrorMapperTests {

    private static final String A_CORRELATION_ID = "a-correlation-id";

    @Test
    @DisplayName("it should build a problem details error object")
    public void shouldBuildAProblemDetailsError() {
        String errorJsonFile = "/payments/401.invalid_signature.json";
        ErrorMapper sut = new ErrorMapper();
        Response<?> invalidClientResponse = buildErrorResponse(errorJsonFile);

        ProblemDetails problemDetails = sut.toProblemDetails(invalidClientResponse);

        ProblemDetails expectedError = deserializeJsonFileTo(errorJsonFile, ProblemDetails.class);
        Assertions.assertEquals(expectedError, problemDetails);
    }

    @Test
    @DisplayName("it should map a legacy error into a problem details")
    public void shouldBuildAProblemDetailsForALegacyError() {
        ErrorMapper sut = new ErrorMapper();
        Response<?> invalidClientResponse = buildErrorResponse("/auth/400.invalid_client.json");

        ProblemDetails actual = sut.toProblemDetails(invalidClientResponse);

        assertEquals(buildGenericError("invalid_client"), actual);
    }

    @Test
    @DisplayName("it should map an unexpected format error into a generic problem details")
    public void shouldBuildAProblemDetailsForAnUnexpectedError() {
        ErrorMapper sut = new ErrorMapper();
        Response<?> invalidClientResponse = buildErrorResponse("internal error".getBytes(StandardCharsets.UTF_8));

        ProblemDetails actual = sut.toProblemDetails(invalidClientResponse);

        assertEquals(buildGenericError(GENERIC_ERROR_TITLE), actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("it should map an IO error into a generic problem details")
    public void shouldBuildAProblemDetailsForAnIOError() {
        ErrorMapper sut = new ErrorMapper();
        Response<?> ioErrorResponse = mock(Response.class);
        Headers headers = mock(Headers.class);
        when(headers.get(TL_CORRELATION_ID)).thenReturn(A_CORRELATION_ID);
        when(ioErrorResponse.headers()).thenReturn(headers);
        when(ioErrorResponse.code()).thenReturn(400);
        when(ioErrorResponse.errorBody()).thenReturn(mock(ResponseBody.class));
        when(ioErrorResponse.errorBody().string()).thenThrow(new IOException());

        ProblemDetails actual = sut.toProblemDetails(ioErrorResponse);

        assertEquals(buildGenericError(GENERIC_ERROR_TITLE), actual);
    }

    @SneakyThrows
    private Response<?> buildErrorResponse(String errorFile) {
        return buildErrorResponse(Files.readAllBytes(Paths.get(JSON_RESPONSES_LOCATION + errorFile)));
    }

    private Response<?> buildErrorResponse(byte[] content) {
        okhttp3.Response rawResponse = Mockito.mock(okhttp3.Response.class);
        ResponseBody responseBody = ResponseBody.create(MediaType.get("application/json"), content);
        when(rawResponse.body()).thenReturn(responseBody);
        when(rawResponse.code()).thenReturn(400);
        Headers headers = mock(Headers.class);
        when(headers.get(TL_CORRELATION_ID)).thenReturn(A_CORRELATION_ID);
        when(rawResponse.headers()).thenReturn(headers);

        return retrofit2.Response.error(responseBody, rawResponse);
    }

    private ProblemDetails buildGenericError(String title) {
        return ProblemDetails.builder()
                .traceId(A_CORRELATION_ID)
                .title(title)
                .status(400)
                .type(GENERIC_ERROR_TYPE)
                .build();
    }
}
