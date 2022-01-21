package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    AUTHORIZATION_REQUIRED("authorization_required"),
    AUTHORIZING("authorizing"),
    AUTHORIZED("authorized"),
    EXECUTED("executed"),
    FAILED("failed"),
    SETTLED("settled");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    @JsonValue
    public String value() {
        return status;
    }
}
