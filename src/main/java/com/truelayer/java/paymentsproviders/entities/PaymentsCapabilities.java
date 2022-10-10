package com.truelayer.java.paymentsproviders.entities;

import lombok.Value;

@Value
public class PaymentsCapabilities {
    BankTransferCapabilities bankTransfer;
}
