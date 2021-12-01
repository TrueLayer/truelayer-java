package truelayer.java.http.entities;

import lombok.Builder;
import lombok.Getter;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Builder
@Getter
public class ApiResponse<T> {
    private final T data;
    private final ProblemDetails error;

    public boolean isError(){
        return isNotEmpty(error);
    }
}