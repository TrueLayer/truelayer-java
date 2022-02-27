package com.truelayer.java.payments.entities.paymentdetail;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ProviderSelection extends AuthorizationFlowAction {
    Type type = Type.PROVIDER_SELECTION;

    List<Provider> providers;
}
