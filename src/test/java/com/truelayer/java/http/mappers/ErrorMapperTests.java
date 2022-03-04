package com.truelayer.java.http.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.http.entities.ProblemDetails;
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

class ErrorMapperTests {

    private static final String A_CORRELATION_ID = "a-correlation-id";

    @Test
    @DisplayName("it should build a problem details error object")
    public void shouldBuildAProblemDetailsError() {
        String errorJsonFile = "/payments/401.invalid_signature.json";
        ErrorMapper sut = new ErrorMapper();
        Response<?> invalidClientResponse = buildErrorResponse(errorJsonFile);

        ProblemDetails problemDetails = sut.toProblemDetails(invalidClientResponse);

        ProblemDetails expectedError = TestUtils.deserializeJsonFileTo(errorJsonFile, ProblemDetails.class);
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

        assertEquals(buildGenericError(ErrorMapper.GENERIC_ERROR_TITLE), actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("it should map an IO error into a generic problem details")
    public void shouldBuildAProblemDetailsForAnIOError() {
        ErrorMapper sut = new ErrorMapper();
        Response<?> ioErrorResponse = mock(Response.class);
        Headers headers = mock(Headers.class);
        when(headers.get(Constants.HeaderNames.TL_CORRELATION_ID)).thenReturn(A_CORRELATION_ID);
        when(ioErrorResponse.headers()).thenReturn(headers);
        when(ioErrorResponse.code()).thenReturn(400);
        when(ioErrorResponse.errorBody()).thenReturn(mock(ResponseBody.class));
        when(ioErrorResponse.errorBody().string()).thenThrow(new IOException());

        ProblemDetails actual = sut.toProblemDetails(ioErrorResponse);

        assertEquals(buildGenericError(ErrorMapper.GENERIC_ERROR_TITLE), actual);
    }

    @Test
    @DisplayName("it should map an no content error response into a generic problem details")
    public void shouldBuildAProblemDetailsForAErrorResponseWithNoContent() {
        ErrorMapper sut = new ErrorMapper();
        Response<?> noContentErrorResponse = mock(Response.class);
        Headers headers = mock(Headers.class);
        when(headers.get(Constants.HeaderNames.TL_CORRELATION_ID)).thenReturn(A_CORRELATION_ID);
        when(noContentErrorResponse.headers()).thenReturn(headers);
        when(noContentErrorResponse.code()).thenReturn(400);

        ProblemDetails actual = sut.toProblemDetails(noContentErrorResponse);

        assertEquals(buildGenericError(ErrorMapper.GENERIC_ERROR_TITLE), actual);
    }

    @SneakyThrows
    private Response<?> buildErrorResponse(String errorFile) {
        return buildErrorResponse(Files.readAllBytes(Paths.get(TestUtils.JSON_RESPONSES_LOCATION + errorFile)));
    }

    private Response<?> buildErrorResponse(byte[] content) {
        okhttp3.Response rawResponse = Mockito.mock(okhttp3.Response.class);
        ResponseBody responseBody = ResponseBody.create(MediaType.get("application/json"), content);
        when(rawResponse.body()).thenReturn(responseBody);
        when(rawResponse.code()).thenReturn(400);
        Headers headers = mock(Headers.class);
        when(headers.get(Constants.HeaderNames.TL_CORRELATION_ID)).thenReturn(A_CORRELATION_ID);
        when(rawResponse.headers()).thenReturn(headers);

        return retrofit2.Response.error(responseBody, rawResponse);
    }

    private ProblemDetails buildGenericError(String title) {
        return ProblemDetails.builder()
                .traceId(A_CORRELATION_ID)
                .title(title)
                .status(400)
                .type(ErrorMapper.GENERIC_ERROR_TYPE)
                .build();
    }
}
