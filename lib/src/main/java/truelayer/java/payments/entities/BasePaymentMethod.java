package truelayer.java.payments.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = BankTransfer.class)
interface BasePaymentMethod {

}
