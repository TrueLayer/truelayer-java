package com.truelayer.quarkusmvc.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResult {
    private String id;
    private String status;
}
