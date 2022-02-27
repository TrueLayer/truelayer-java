package com.truelayer.java.payments.entities.paymentdetail;

import static com.truelayer.java.payments.entities.paymentdetail.RedirectStatus.Type.SUPPORTED;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SupportedRedirectStatus extends RedirectStatus {
    Type type = SUPPORTED;

    URI returnUri;
}
