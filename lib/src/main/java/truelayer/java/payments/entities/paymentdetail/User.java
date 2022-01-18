package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;

    private String name;

    private Optional<String> email;

    private Optional<String> phone;
}
