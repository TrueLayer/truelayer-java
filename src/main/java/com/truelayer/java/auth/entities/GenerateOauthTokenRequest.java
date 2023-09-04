package com.truelayer.java.auth.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.List;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class GenerateOauthTokenRequest {
    private String clientId;

    private String clientSecret;

    private final GrantType grantType = GrantType.CLIENT_CREDENTIALS;

    @Singular
    @JsonSerialize(using = ScopesSerializer.class)
    @JsonProperty("scope")
    private List<String> scopes;

    @RequiredArgsConstructor
    @Getter
    public enum GrantType {
        CLIENT_CREDENTIALS("client_credentials");

        @JsonValue
        private final String type;
    }

    public static class ScopesSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(String.join(" ", value));
        }
    }
}
