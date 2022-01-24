package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;

    private String name;

    private String email;

    private String phone;

    @JsonGetter("email")
    private Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    @JsonGetter("phone")
    private Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }
}
