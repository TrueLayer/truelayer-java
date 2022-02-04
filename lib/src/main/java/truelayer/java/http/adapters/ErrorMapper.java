package truelayer.java.http.adapters;

import static truelayer.java.common.Constants.HeaderNames.TL_CORRELATION_ID;
import static truelayer.java.common.Utils.getObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.ObjectUtils;
import retrofit2.Response;
import truelayer.java.http.adapters.entities.LegacyError;
import truelayer.java.http.entities.ProblemDetails;

public class ErrorMapper {
    public static final String GENERIC_ERROR_TITLE = "server_error";
    public static final String GENERIC_ERROR_TYPE = "https://docs.truelayer.com/docs/error-types";

    public <T> ProblemDetails toProblemDetails(Response<T> response) {
        final String correlationId = tryGetCorrelationId(response.headers());

        final ResponseBody errorBody = response.errorBody();
        if (ObjectUtils.isEmpty(errorBody)) {
            return buildFallbackError(response.code(), correlationId, GENERIC_ERROR_TITLE);
        }

        final String errorBodyString;
        try {
            errorBodyString = errorBody.string();
        } catch (IOException e) {
            return buildFallbackError(response.code(), correlationId, GENERIC_ERROR_TITLE);
        }

        try {
            return getObjectMapper().readValue(errorBodyString, ProblemDetails.class);
        } catch (JsonProcessingException e) {
            return tryMapLegacyError(response.code(), correlationId, errorBodyString);
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

    private String tryGetCorrelationId(Headers headers) {
        if (ObjectUtils.isEmpty(headers)) {
            return null;
        }
        return headers.get(TL_CORRELATION_ID);
    }
}
