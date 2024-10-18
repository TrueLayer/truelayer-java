package com.truelayer.java.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Optional;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Address {
    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String zip;

    private String countryCode;

    @JsonGetter
    public Optional<String> getAddressLine2() {
        return Optional.ofNullable(addressLine2);
    }
}
