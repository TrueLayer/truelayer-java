package com.truelayer.java.paymentsproviders.entities;

import java.util.List;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Form {
    private List<InputType> inputTypes;
}
