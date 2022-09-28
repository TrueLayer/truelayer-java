package com.truelayer.java.payments.entities.paymentdetail.forminput.image;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Uri extends Image {
    Type type = Type.URI;

    String uri;
}
