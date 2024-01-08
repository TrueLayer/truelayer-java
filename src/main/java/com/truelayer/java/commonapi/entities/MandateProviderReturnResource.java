package com.truelayer.java.commonapi.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class MandateProviderReturnResource extends ProviderReturnResource {
    Type type = Type.MANDATE;

    String mandateId;
}
