package truelayer.java.http.entities;

import lombok.*;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ApiResponse<T> {
    private final T data;
    private final ProblemDetails error;

    public boolean isError(){
        return isNotEmpty(error);
    }
}