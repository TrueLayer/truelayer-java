package com.truelayer.java.commonapi.entities;

import lombok.Value;

@Value
public class SubmitPaymentReturnParametersResponse {

    Resource resource;

    @Value
    public static class Resource {
        String type;

        String paymentId;
    }
}
