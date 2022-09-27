package com.truelayer.java.payments.entities;

import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SubmitFormRequest {
    private Map<String, String> inputs;
}
