package com.truelayer.java.payments.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SubmitFormRequest {
    private Map<String, String> inputs;
}
