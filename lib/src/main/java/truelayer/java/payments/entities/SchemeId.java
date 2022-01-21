package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SchemeId {
    POLISH_DOMESTIC_STANDARD("polish_domestic_standard"),
    POLISH_DOMESTIC_EXPRESS("polish_domestic_express"),
    FASTER_PAYMENT_SERVICE("faster_payment_service"),
    SWIFT_TRANSFER("swift_transfer"),
    SEPA_CREDIT_TRANSFER("sepa_credit_transfer"),
    SEPA_CREDIT_TRANSFER_INSTANT("sepa_credit_transfer_instant"),
    PROVIDER_DETERMINED("provider_determined"),
    NORWEGIAN_DOMESTIC_CREDIT_TRANSFER("norwegian_domestic_credit_transfer"),
    SWEDISH_DOMESTIC_CREDIT_TRANSFER("swedish_domestic_credit_transfer");

    private final String schemeId;

    SchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    @JsonValue
    public String getSchemeId() {
        return schemeId;
    }
}
