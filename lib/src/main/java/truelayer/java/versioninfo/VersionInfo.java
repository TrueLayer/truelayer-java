package truelayer.java.versioninfo;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class VersionInfo {
    private String libraryName;
    private String libraryVersion;
}
