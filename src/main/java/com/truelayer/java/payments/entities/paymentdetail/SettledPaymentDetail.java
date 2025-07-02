package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import com.truelayer.java.entities.PaymentSource;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SettledPaymentDetail extends PaymentDetail {

    Status status = Status.SETTLED;

    PaymentSource paymentSource;

    ZonedDateTime succeededAt;

    ZonedDateTime settledAt;

    ZonedDateTime executedAt;

    ZonedDateTime creditableAt;

    AuthorizationFlowWithConfiguration authorizationFlow;

    SettlementRisk settlementRisk;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }

    @JsonGetter
    public Optional<SettlementRisk> getSettlementRisk() {
        return Optional.ofNullable(settlementRisk);
    }

    @Value
    @EqualsAndHashCode
    public static class SettlementRisk {
        Category category;

        @RequiredArgsConstructor
        @Getter
        public enum Category {
            LOW_RISK("low_risk"),
            HIGH_RISK("high_risk");

            @JsonValue
            private final String category;
        }
    }
}
