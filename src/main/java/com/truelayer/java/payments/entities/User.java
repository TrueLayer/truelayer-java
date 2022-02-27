package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
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

    @JsonGetter
    private Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @JsonGetter
    private Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }
}
