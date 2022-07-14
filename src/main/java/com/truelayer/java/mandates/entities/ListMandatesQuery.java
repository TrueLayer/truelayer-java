package com.truelayer.java.mandates.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class ListMandatesQuery {
    private String userId;

    private String cursor;

    private int limit;
}
