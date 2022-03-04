package com.truelayer.java.payments.entities.paymentdetail;

import static com.truelayer.java.payments.entities.paymentdetail.RedirectStatus.Type.*;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class NotSupportedRedirectStatus extends RedirectStatus {
    Type type = NOT_SUPPORTED;
}
