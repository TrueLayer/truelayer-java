package com.truelayer.java.http.entities;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import lombok.*;

/**
 * Model for API responses object. Instances of this class can contain either a data or an error object.
 * Comes with a <code>isError()</code> utility method to easily understand whether the object holds a successful or an error response.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ApiResponse<T> {
    private final T data;
    private final ProblemDetails error;

    /**
     * Utility method to easily understand whether the object holds a successful or an error response.
     * @return true if the response contains an error.
     */
    public boolean isError() {
        return isNotEmpty(error);
    }
}
