package com.truelayer.java.signupplus.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Optional;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
public class UserData {
    String title;

    String firstName;

    String lastName;

    String dateOfBirth;

    Address address;

    String nationalIdentificationNumber;

    Sex sex;

    AccountDetails accountDetails;

    @JsonGetter
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    @JsonGetter
    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    @JsonGetter
    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    @JsonGetter
    public Optional<String> getDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    @JsonGetter
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    @JsonGetter
    public Optional<String> getNationalIdentificationNumber() {
        return Optional.ofNullable(nationalIdentificationNumber);
    }

    @JsonGetter
    public Optional<Sex> getSex() {
        return Optional.ofNullable(sex);
    }

    @JsonGetter
    public Optional<AccountDetails> getAccountDetails() {
        return Optional.ofNullable(accountDetails);
    }
}
