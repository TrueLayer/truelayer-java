package com.truelayer.java.entities;

import java.util.List;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
public class RequestScopes {
    @Singular
    private final List<String> scopes;
}
