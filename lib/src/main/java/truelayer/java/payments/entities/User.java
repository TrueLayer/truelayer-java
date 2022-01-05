package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @JsonProperty("type")
    private Type type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    public enum Type {
        EXISTING("existing"),
        NEW("new");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @JsonValue
        public String getType() {
            return type;
        }
    }
}
