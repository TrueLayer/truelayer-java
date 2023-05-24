package com.truelayer.java.http.mappers;

import static com.truelayer.java.Utils.getObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.truelayer.java.Constants;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.http.mappers.entities.LegacyError;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.ObjectUtils;
import retrofit2.Response;

public class ErrorMapper {
    public static final String GENERIC_ERROR_TITLE = "server_error";
    public static final String GENERIC_ERROR_TYPE = "https://docs.truelayer.com/docs/error-types";

    public static <T> ProblemDetails toProblemDetails(Response<T> response) {
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

        ProblemDetails problemDetails;
        try {
            problemDetails = getObjectMapper().readValue(errorBodyString, ProblemDetails.class);
        } catch (JsonProcessingException e) {
            return tryMapLegacyError(response.code(), correlationId, errorBodyString);
        }

        return problemDetails.isWellFormed()
                ? problemDetails
                : tryMapLegacyError(response.code(), correlationId, errorBodyString);
    }

    private static ProblemDetails tryMapLegacyError(int code, String correlationId, String errorBody) {
        try {
            LegacyError legacyError = getObjectMapper().readValue(errorBody, LegacyError.class);
            return buildFallbackError(code, correlationId, legacyError.getError());
        } catch (JsonProcessingException e) {
            return buildFallbackError(code, correlationId, GENERIC_ERROR_TITLE);
        }
    }

    private static ProblemDetails buildFallbackError(int code, String correlationId, String title) {
        return ProblemDetails.builder()
                .status(code)
                .title(title)
                .traceId(correlationId)
                .type(GENERIC_ERROR_TYPE)
                .build();
    }

    private static String tryGetCorrelationId(Headers headers) {
        if (ObjectUtils.isEmpty(headers)) {
            return null;
        }
        return headers.get(Constants.HeaderNames.TL_CORRELATION_ID);
    }
}
