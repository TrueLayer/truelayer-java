package com.truelayer.java.commonapi.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SubmitPaymentReturnParametersRequest {
    private String query;

    private String fragment;
}
