package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReleaseChannel {
    GENERAL_AVAILABILITY("general_availability"),
    PUBLIC_BETA("public_beta"),
    PRIVATE_BETA("private_beta");

    private final String releaseChannel;

    ReleaseChannel(String releaseChannel) {
        this.releaseChannel = releaseChannel;
    }

    @JsonValue
    public String getReleaseChannel() {
        return releaseChannel;
    }
}
