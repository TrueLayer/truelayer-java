package com.truelayer.java.payments.entities.paymentdetail.forminput;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class Select extends Input {
    Type type = Type.SELECT;

    List<Option> options;

    @Value
    @EqualsAndHashCode
    public static class Option {
        String id;
        DisplayText displayText;
    }
}
