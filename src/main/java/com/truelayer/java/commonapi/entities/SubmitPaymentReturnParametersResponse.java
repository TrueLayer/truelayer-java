package com.truelayer.java.commonapi.entities;

import lombok.Value;

@Value
public class SubmitPaymentReturnParametersResponse {

    Resource resource;

    // TODO: need to model mandates as well!
    @Value
    public static class Resource {
        String type;

        String paymentId;
    }
}
