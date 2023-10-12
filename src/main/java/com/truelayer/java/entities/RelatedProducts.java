package com.truelayer.java.entities;

import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class RelatedProducts {

    private Map<String, String> signupPlus;
}
