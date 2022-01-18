package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    AUTHORIZATION_REQUIRED("authorization_required"),
    AUTHORIZING("authorizing"),
    AUTHORIZED("authorized"),
    SUCCEEDED("succeeded"),
    FAILED("failed"),
    SETTLED("settled");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
