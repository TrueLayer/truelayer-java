package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonDeserialize(as = BankTransfer.class)
@ToString
@EqualsAndHashCode
public abstract class PaymentSchemeIdentifier {}
