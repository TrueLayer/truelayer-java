package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationFlowAuthorizing extends AuthorizationFlowResponse {

    Status status = Status.AUTHORIZING;

    @JsonIgnore
    @Override
    public AuthorizationFlow getAuthorizationFlow() {
        return authorizationFlow;
    }
}
