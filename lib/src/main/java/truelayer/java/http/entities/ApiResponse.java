package truelayer.java.http.entities;

import lombok.Builder;
import lombok.Getter;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

//todo review all lombok annotations. to see if we can have a generic ready builder

@Builder
@Getter
public class ApiResponse<T> {
    private final T data;
    private final ProblemDetails error;

    public boolean isError(){
        return isNotEmpty(error);
    }
}