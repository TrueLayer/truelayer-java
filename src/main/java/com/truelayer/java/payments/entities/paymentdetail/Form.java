package com.truelayer.java.payments.entities.paymentdetail;

import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
public class Form extends AuthorizationFlowAction {
    Type type = Type.FORM;

    List<Input.Type> inputs;
}
