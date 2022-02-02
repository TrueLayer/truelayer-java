package truelayer.java.http.adapters;

import static truelayer.java.common.Constants.HeaderNames.TL_CORRELATION_ID;
import static truelayer.java.common.Utils.getObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import retrofit2.Response;
import truelayer.java.http.adapters.entities.LegacyError;
import truelayer.java.http.entities.ProblemDetails;

public class ErrorMapper {
    public static final String GENERIC_ERROR_TITLE = "server_error";
    public static final String GENERIC_ERROR_TYPE = "https://docs.truelayer.com/docs/error-types";

    public <T> ProblemDetails toProblemDetails(Response<T> response) {
        final String correlationId = response.headers().get(TL_CORRELATION_ID);

        String errorBody;
        try {
            errorBody = response.errorBody().string();
        } catch (IOException e) {
            return buildFallbackError(response.code(), correlationId, GENERIC_ERROR_TITLE);
        }

        try {
            return getObjectMapper().readValue(errorBody, ProblemDetails.class);
        } catch (JsonProcessingException e) {
            return tryMapLegacyError(response.code(), correlationId, errorBody);
        }
    }

    private ProblemDetails tryMapLegacyError(int code, String correlationId, String errorBody) {
        try {
            LegacyError legacyError = getObjectMapper().readValue(errorBody, LegacyError.class);
            return buildFallbackError(code, correlationId, legacyError.getError());
        } catch (JsonProcessingException e) {
            return buildFallbackError(code, correlationId, GENERIC_ERROR_TITLE);
        }
    }

    private ProblemDetails buildFallbackError(int code, String correlationId, String title) {
        return ProblemDetails.builder()
                .status(code)
                .title(title)
                .traceId(correlationId)
                .type(GENERIC_ERROR_TYPE)
                .build();
    }
}
