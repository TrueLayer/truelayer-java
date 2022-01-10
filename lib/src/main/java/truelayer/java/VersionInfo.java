package truelayer.java;

import lombok.Builder;
import lombok.Data;

/**
 * Holds basic library information of app name and version
 * filled with values contained in version.properties file
 */
@Builder
@Data
public class VersionInfo {
    private String name;
    private String version;

    public static class Keys {
        public static String NAME = "name";
        public static String VERSION = "version";
    }
}
