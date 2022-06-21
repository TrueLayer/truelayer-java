package com.truelayer.java.mandates.entities.mandatedetail;

import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class RevokedMandateDetail extends MandateDetail {

    Status status = Status.REVOKED;

    String revocationSource;

    ZonedDateTime authorizedAt;

    ZonedDateTime revokedAt;

    AuthorizationFlowWithConfiguration authorizationFlow;
}
