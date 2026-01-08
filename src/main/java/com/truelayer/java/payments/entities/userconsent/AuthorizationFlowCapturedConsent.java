package com.truelayer.java.payments.entities.userconsent;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class AuthorizationFlowCapturedConsent extends UserConsent {
    private final UserConsent.Type type = Type.AUTHORIZATION_FLOW_CAPTURED;
}
