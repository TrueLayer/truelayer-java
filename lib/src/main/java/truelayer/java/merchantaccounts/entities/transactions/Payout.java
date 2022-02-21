package truelayer.java.merchantaccounts.entities.transactions;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.beneficiary.Beneficiary;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = PendingPayout.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PendingPayout.class, name = "pending"),
        @JsonSubTypes.Type(value = ExecutedPayout.class, name = "settled"),
})
@Getter
public abstract class Payout {

    @RequiredArgsConstructor
    @Getter
    public enum ContextCode {
        WITHDRAWAL("withdrawal"),
        SERVICE_PAYMENT("service_payment"),
        INTERNAL("internal");

        @JsonValue
        private final String status;
    }
}
