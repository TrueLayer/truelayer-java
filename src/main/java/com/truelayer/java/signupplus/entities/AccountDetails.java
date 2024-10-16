package com.truelayer.java.signupplus.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Optional;
import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class AccountDetails {
    String accountNumber;

    String sortCode;

    String iban;

    String providerId;

    @JsonGetter
    public Optional<String> getAccountNumber() {
        return Optional.ofNullable(accountNumber);
    }

    @JsonGetter
    public Optional<String> getSortCode() {
        return Optional.ofNullable(sortCode);
    }

    @JsonGetter
    public Optional<String> getIban() {
        return Optional.ofNullable(iban);
    }

    @JsonGetter
    public Optional<String> getProviderId() {
        return Optional.ofNullable(providerId);
    }
}
