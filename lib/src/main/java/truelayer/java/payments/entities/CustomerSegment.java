package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerSegment {
    RETAIL("retail"),
    BUSINESS("business"),
    CORPORATE("corporate");

    private final String customerSegment;

    CustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonValue
    public String getCustomerSegment() {
        return customerSegment;
    }
}
