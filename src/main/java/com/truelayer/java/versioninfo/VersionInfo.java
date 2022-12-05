package com.truelayer.java.versioninfo;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class VersionInfo {
    private static final String JAVA_VERSION_ENV_VARIABLE = "java.version";

    private String libraryName;
    private String libraryVersion;
    private final String javaVersion = System.getProperty(JAVA_VERSION_ENV_VARIABLE);
}
