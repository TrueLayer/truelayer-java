package truelayer.java.http.adapters;

import static truelayer.java.common.Utils.getObjectMapper;

import java.io.IOException;
import retrofit2.Response;
import truelayer.java.http.entities.ProblemDetails;

// todo tests for this class: deliberately postponed as we need to solve the implementation doubts first
public class ErrorMapper {
    private ErrorMapper() {}

    public static ProblemDetails fromResponse(Response response) {
        String errorBody = "error";
        try {
            errorBody = response.errorBody().string();
            return getObjectMapper().readValue(errorBody, ProblemDetails.class);
        } catch (IOException e) {
            // todo how to properly log this ?
            e.printStackTrace();
            return ProblemDetails.builder().type("error").detail(errorBody).build();
        }
    }
}
