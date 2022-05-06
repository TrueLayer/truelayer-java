package com.truelayer.java;

import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConnectionPoolOptions {

    @Builder.Default
    private int maxIdleConnections = 5;

    @Builder.Default
    private KeepAliveDuration keepAliveDuration = KeepAliveDuration.builder().build();

    @Builder
    @Getter
    public static class KeepAliveDuration {
        @Builder.Default
        private long duration = 5;

        @Builder.Default
        private TimeUnit timeUnit = TimeUnit.MINUTES;
    }
}
