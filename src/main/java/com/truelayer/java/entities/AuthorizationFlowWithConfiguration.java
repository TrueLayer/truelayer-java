package com.truelayer.java.entities;

import com.truelayer.java.payments.entities.AuthorizationFlow;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationFlowWithConfiguration extends AuthorizationFlow {

    Configuration configuration;
}
