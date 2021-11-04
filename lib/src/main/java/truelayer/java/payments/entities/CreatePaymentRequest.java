package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class CreatePaymentRequest {

    @JsonProperty("amount_in_minor")
    private int amountInMinor;

    private String currency;

    @JsonProperty("payment_method")
    private Method paymentMethod;

    private Beneficiary beneficiary;

    private User user;

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class Method {

        private String type; //todo: enum ?

        @JsonProperty("statement_reference")
        private String statementReference;

        @JsonProperty("provider_filter")
        private ProviderFilter providerFilter;


    }

    @Builder
    @JsonInclude(Include.NON_NULL)
    public static class ProviderFilter{
        @JsonProperty("release_channel")
        private String releaseChannel;
        //todo complete
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class Beneficiary {

        private String type; //todo: enum ?

        private String id;

        private String name;
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class User {

        private String type;

        private String name;

        private String email;

        private String phone;
    }
}
