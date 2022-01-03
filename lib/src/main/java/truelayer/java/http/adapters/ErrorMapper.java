package truelayer.java.http.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;
import truelayer.java.http.entities.ProblemDetails;

import java.io.IOException;

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
        try {
            return getInstance().readValue(response.errorBody().string(), ProblemDetails.class);
        } catch (IOException e) {
            //todo how to properly log this ?
            e.printStackTrace();
            return fromThrowable(e);
        }
    }

    public static ProblemDetails fromThrowable(Throwable throwable){
        //todo how to deal with non problem details errors ?
        // auth api does not us this convention RN
        return ProblemDetails.builder()
                .title(throwable.getMessage())
                .build();
    }
}
