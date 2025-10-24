package com.truelayer.java.entities;

import static com.truelayer.java.entities.HostedPageType.HP2;
import static com.truelayer.java.entities.HostedPageType.HPP;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResourceType {
    PAYMENT("payments", "payment_id", HPP),
    MANDATE("mandates", "mandate_id", HPP),
    PAYOUT("payouts", "payout_id", HP2),
    ;

    private final String hppLinkPath;
    private final String hppLinkQueryParameter;
    private final HostedPageType hostedPageType;
}
