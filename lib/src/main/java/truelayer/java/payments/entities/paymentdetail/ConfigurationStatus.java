package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConfigurationStatus {
    SUPPORTED("supported"),
    NOT_SUPPORTED("not_supported");

    private final String status;

    ConfigurationStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String value() {
        return status;
    }
}
