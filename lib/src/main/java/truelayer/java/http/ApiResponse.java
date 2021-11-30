package truelayer.java.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

//todo review all lombok annotations. to see if we can have a generic ready builder
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final T data;
    private final ProblemDetails error;

    public boolean isError(){
        return ObjectUtils.isNotEmpty(error);
    }

    @Builder
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true) //todo review
    public static class ProblemDetails{
        private String type;
        private String title;
        private String detail;
        private Integer status;
        @JsonProperty("trace_id")
        private String traceId;
    }
}