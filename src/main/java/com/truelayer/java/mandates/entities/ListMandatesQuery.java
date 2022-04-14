package com.truelayer.java.mandates.entities;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ListMandatesQuery {
    private String userId;

    private String cursor;

    private int limit;
}
