package truelayer.java.http.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static truelayer.java.TestUtils.JSON_RESPONSES_LOCATION;
import static truelayer.java.TestUtils.deserializeJsonFileTo;

import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tinylog.TaggedLogger;
import retrofit2.Response;
import truelayer.java.http.entities.ProblemDetails;

class ErrorMapperTests {

    @SneakyThrows
    @Test
    @DisplayName("it should build a problem details error object")
    public void shouldBuildAProblemDetailsError() {
        var logger = mock(TaggedLogger.class);
        var errorJsonFile = "/payments/401.invalid_signature.json";
        var sut = new ErrorMapper(logger);
        var invalidClientResponse = buildErrorResponse(errorJsonFile);

        var problemDetails = sut.toProblemDetails(invalidClientResponse);

        var expectedError = deserializeJsonFileTo(errorJsonFile, ProblemDetails.class);
        Assertions.assertEquals(expectedError, problemDetails);
        verify(logger, never()).warn(anyString(), any(Throwable.class));
    }

    @SneakyThrows
    @Test
    @DisplayName("it should build a fallback problem details error object and log a warning")
    public void shouldBuildAFallbackProblemDetailsError() {
        var logger = mock(TaggedLogger.class);
        var sut = new ErrorMapper(logger);
        var invalidClientResponse = buildErrorResponse("/auth/400.invalid_client.json");

        var problemDetails = sut.toProblemDetails(invalidClientResponse);

        assertTrue(problemDetails.getDetail().contains("invalid_client"));
        verify(logger, times(1)).warn(anyString(), any(Throwable.class));
        verifyNoMoreInteractions(logger);
    }

    @SneakyThrows
    private Response buildErrorResponse(String errorFile) {
        return retrofit2.Response.error(
                400,
                ResponseBody.create(
                        MediaType.get("application/json"),
                        Files.readString(Path.of(new StringBuilder(JSON_RESPONSES_LOCATION)
                                .append(errorFile)
                                .toString()))));
    }
}
