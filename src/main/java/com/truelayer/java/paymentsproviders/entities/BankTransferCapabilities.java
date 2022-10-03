package com.truelayer.java.paymentsproviders.entities;

import com.truelayer.java.payments.entities.ReleaseChannel;
import java.util.List;
import lombok.Value;

@Value
public class BankTransferCapabilities {
    ReleaseChannel releaseChannel;

    List<Scheme> schemes;
}
