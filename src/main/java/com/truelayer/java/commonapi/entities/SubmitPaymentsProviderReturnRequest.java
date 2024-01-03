package com.truelayer.java.commonapi.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SubmitPaymentsProviderReturnRequest {
    private String query;

    private String fragment;
}
