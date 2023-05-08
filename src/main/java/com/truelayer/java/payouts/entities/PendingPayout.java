package com.truelayer.java.payouts.entities;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.SchemeId;
import com.truelayer.java.payouts.entities.beneficiary.Beneficiary;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = false)
public class PendingPayout extends Payout{
    Status status = Status.PENDING;
}
