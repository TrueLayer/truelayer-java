package com.truelayer.java.signupplus.entities;

import java.net.URI;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
public class GenerateAuthUriResponse {
    URI authUri;
}
