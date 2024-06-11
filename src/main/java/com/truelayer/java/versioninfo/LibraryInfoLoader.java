package com.truelayer.java.versioninfo;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import java.io.IOException;
import java.util.Properties;

/**
 * Class the loads the version of the library during the client initialization.
 */
public class LibraryInfoLoader {
    private static final String CONFIG_FILE_PREXIF = "truelayer-java";

    public VersionInfo load() {
        Properties libraryVersionProps = getGradleProperties();

        return VersionInfo.builder()
                .libraryName(libraryVersionProps.getProperty(Constants.VersionInfo.NAME))
                .libraryVersion(libraryVersionProps.getProperty(Constants.VersionInfo.VERSION))
                .build();
    }

    private Properties getGradleProperties() {
        try {
            Properties versionInfoProps = new Properties();
            versionInfoProps.load(getClass()
                    .getClassLoader()
                    .getResourceAsStream(CONFIG_FILE_PREXIF + "." + "version" + ".properties"));
            return versionInfoProps;
        } catch (IOException e) {
            throw new TrueLayerException("Unable to load library version file", e);
        }
    }
}
