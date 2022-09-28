package com.truelayer.java.payments.entities.paymentdetail.forminput;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class Text extends TextBase {
    Type type = Type.TEXT;
}
