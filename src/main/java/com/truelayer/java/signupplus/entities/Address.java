package com.truelayer.java.signupplus.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Optional;
import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class Address {
    String addressLine1;

    String addressLine2;

    String city;

    String state;

    String zip;

    String country;

    @JsonGetter
    public Optional<String> getAddressLine1() {
        return Optional.ofNullable(addressLine1);
    }

    @JsonGetter
    public Optional<String> getAddressLine2() {
        return Optional.ofNullable(addressLine2);
    }

    @JsonGetter
    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    @JsonGetter
    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    @JsonGetter
    public Optional<String> getZip() {
        return Optional.ofNullable(zip);
    }

    @JsonGetter
    public Optional<String> getCountry() {
        return Optional.ofNullable(country);
    }
}
