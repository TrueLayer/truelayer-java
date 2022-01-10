package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @JsonProperty("id")
    private String id;

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
