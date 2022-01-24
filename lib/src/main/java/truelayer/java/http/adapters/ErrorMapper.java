package truelayer.java.http.adapters;

import static truelayer.java.common.Utils.getObjectMapper;

import java.io.IOException;
import org.tinylog.TaggedLogger;
import retrofit2.Response;
import truelayer.java.http.entities.ProblemDetails;

public class ErrorMapper {
    private final TaggedLogger logger;

    protected ErrorMapper(TaggedLogger logger) {
        this.logger = logger;
    }

    public ProblemDetails toProblemDetails(Response response) {
        String errorBody = "error";
        try {
            errorBody = response.errorBody().string();
            return getObjectMapper().readValue(errorBody, ProblemDetails.class);
        } catch (IOException e) {
            logger.warn("failed to reliably deserialize error into problem details, fallback applied: {}", e);
            return ProblemDetails.builder().type("error").detail(errorBody).build();
        }
    }
}
