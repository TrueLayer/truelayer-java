package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

@Builder
@ToString
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class CreatePaymentRequest {
    @JsonProperty // these annotations are required for proper serialization,
    // regardless of the generic strategy used
    private int amountInMinor;
    @JsonProperty
    private String currency;
    @JsonProperty
    private BasePaymentMethod paymentMethod;
    @JsonProperty
    private BaseBeneficiary beneficiary;
    @JsonProperty
    private User user;

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        @JsonProperty
        private String id;
        @JsonProperty
        private User.Type type;
        @JsonProperty
        private String name;
        @JsonProperty
        private String email;
        @JsonProperty
        private String phone;

        public enum Type {
            EXISTING("existing"),
            NEW("new");

            private final String type;

            Type(String type) {
                this.type = type;
            }

            @JsonValue
            public String getType() {
                return type;
            }
        }
    }
}
