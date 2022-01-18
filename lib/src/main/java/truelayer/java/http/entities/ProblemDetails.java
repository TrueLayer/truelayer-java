package truelayer.java.http.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ProblemDetails {
    private String type;
    private String title;
    private String detail;
    private Integer status;
    private String traceId;
}
