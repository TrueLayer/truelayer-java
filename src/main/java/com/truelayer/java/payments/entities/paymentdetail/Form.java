package com.truelayer.java.payments.entities.paymentdetail;

import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Form extends AuthorizationFlowAction {
    Type type = Type.FORM;

    List<Input> inputs;
}
