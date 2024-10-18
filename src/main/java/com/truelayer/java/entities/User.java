package com.truelayer.java.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import java.time.LocalDate;
import java.util.Optional;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class User {
    private String id;

    private String name;

    private String email;

    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Address address;

    @JsonGetter
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @JsonGetter
    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    @JsonGetter
    public Optional<LocalDate> getDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    @JsonGetter
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }
}
