package com.truelayer.java.payments.entities.paymentdetail.forminput.image;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Base64 extends Image {
    Type type = Type.BASE_64;

    String data;
    String mediaType;
}
