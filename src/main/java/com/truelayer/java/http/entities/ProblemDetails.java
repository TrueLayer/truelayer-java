package com.truelayer.java.http.entities;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Model for errors in the Problem Details format.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7807">Problem Details RFC</a>
 */
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
    private JsonNode errors;

    @JsonIgnore
    public boolean isWellFormed() {
        return allNotNull(type, title, status);
    }
}
