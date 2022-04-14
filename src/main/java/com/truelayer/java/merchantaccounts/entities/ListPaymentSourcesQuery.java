package com.truelayer.java.merchantaccounts.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class ListPaymentSourcesQuery {
    private String userId;
}
