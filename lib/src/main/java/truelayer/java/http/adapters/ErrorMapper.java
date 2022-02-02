package truelayer.java.http.adapters;

import static truelayer.java.common.Constants.HeaderNames.TL_CORRELATION_ID;
import static truelayer.java.common.Utils.getObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.tinylog.TaggedLogger;
import retrofit2.Response;
import truelayer.java.http.adapters.entities.LegacyError;
import truelayer.java.http.entities.ProblemDetails;

public class ErrorMapper {
    private final TaggedLogger logger;

    public static final String GENERIC_ERROR_TITLE = "server_error";
    public static final String GENERIC_ERROR_TYPE = "https://docs.truelayer.com/docs/error-types";

    protected ErrorMapper(TaggedLogger logger) {
        this.logger = logger;
    }

    public <T> ProblemDetails toProblemDetails(Response<T> response) {
        final String correlationId = response.headers().get(TL_CORRELATION_ID);

        String errorBody;
        try {
            errorBody = response.errorBody().string();
        } catch (Exception e) {
            logger.warn("unexpected IO error, generic error returned: {}", e);
            return buildFallbackError(response.code(), correlationId, GENERIC_ERROR_TITLE);
        }

        try {
            return getObjectMapper().readValue(errorBody, ProblemDetails.class);
        } catch (JsonProcessingException e) {
            logger.warn("received an unexpected error format, will try to apply fallback for legacy error: {}", e);
        }

        return tryMapLegacyError(response.code(), correlationId, errorBody);
    }

    private ProblemDetails tryMapLegacyError(int code, String correlationId, String errorBody) {
        try {
            LegacyError legacyError = getObjectMapper().readValue(errorBody, LegacyError.class);
            return buildFallbackError(code, correlationId, legacyError.getError());
        } catch (JsonProcessingException e) {
            logger.warn("unable to map legacy error. generic error defaults returned: {}", e);
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
