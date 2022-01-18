package truelayer.java.http.entities;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ApiResponse<T> {
    private final T data;
    private final ProblemDetails error;

    public boolean isError() {
        return isNotEmpty(error);
    }
}
