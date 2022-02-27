package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SchemeId {
    POLISH_DOMESTIC_STANDARD("polish_domestic_standard"),
    POLISH_DOMESTIC_EXPRESS("polish_domestic_express"),
    FASTER_PAYMENTS_SERVICE("faster_payments_service"),
    SWIFT_TRANSFER("swift_transfer"),
    SEPA_CREDIT_TRANSFER("sepa_credit_transfer"),
    SEPA_CREDIT_TRANSFER_INSTANT("sepa_credit_transfer_instant"),
    PROVIDER_DETERMINED("provider_determined"),
    NORWEGIAN_DOMESTIC_CREDIT_TRANSFER("norwegian_domestic_credit_transfer"),
    SWEDISH_DOMESTIC_CREDIT_TRANSFER("swedish_domestic_credit_transfer");

    @JsonValue
    private final String schemeId;
}
