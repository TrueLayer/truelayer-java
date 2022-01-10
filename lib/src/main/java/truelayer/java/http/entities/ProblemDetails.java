package truelayer.java.http.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ProblemDetails{
    @JsonProperty("type")
    private String type;
    @JsonProperty("title")
    private String title;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("trace_id")
    private String traceId;
}