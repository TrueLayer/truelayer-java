package com.truelayer.java.payments.entities.userconsent;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class PrecapturedConsent extends UserConsent {
    private final UserConsent.Type type = Type.PRECAPTURED;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ZonedDateTime capturedAt;
}
