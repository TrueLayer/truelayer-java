package truelayer.java.http.adapters;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;
import truelayer.java.http.entities.ProblemDetails;

import java.io.IOException;

//todo tests for this class: deliberately postponed as we need to solve the implementation doubts first
public class ErrorMapper {
    private static ObjectMapper ObjectMapperInstance = null;

    private ErrorMapper(){}

    private static ObjectMapper getInstance(){
        if(ObjectMapperInstance == null){
            ObjectMapperInstance = new ObjectMapper();
        }
        return ObjectMapperInstance;
    }

    public static ProblemDetails fromResponse(Response response){
        String errorBody = "error";
        try {
            errorBody = response.errorBody().string();
            return getInstance().readValue(errorBody, ProblemDetails.class);
        } catch (IOException e) {
            //todo how to properly log this ?
            e.printStackTrace();
            return ProblemDetails.builder()
                    .type("error")
                    .detail(errorBody)
                    .build();
        }
    }

    public static ProblemDetails fromThrowable(Throwable throwable){
        return ProblemDetails.builder()
                .type("error")
                .detail(throwable.getMessage())
                .build();
    }
}
