package com.truelayer.java.versioninfo;

import static java.util.Objects.requireNonNull;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Class the loads the version of the library during the client initialization.
 * This component leverages a gradle build steps that prepares a truelayer-java.version.properties
 * file in project's resources directory.
 */
public class LibraryInfoLoader {
    private static final String CONFIG_FILE_PREXIF = "truelayer-java";

    public VersionInfo load() {
        Properties libraryVersionProps = getVersionInfoProperties();

        return VersionInfo.builder()
                .libraryName(libraryVersionProps.getProperty(Constants.VersionInfo.NAME))
                .libraryVersion(libraryVersionProps.getProperty(Constants.VersionInfo.VERSION))
                .build();
    }

    private Properties getVersionInfoProperties() {
        try {
            Properties versionInfoProps = new Properties();
            versionInfoProps.load(Files.newInputStream(Paths.get(requireNonNull(getClass()
                            .getClassLoader()
                            .getResource(CONFIG_FILE_PREXIF + "." + "version" + ".properties"))
                    .getPath())));
            return versionInfoProps;
        } catch (IOException e) {
            throw new TrueLayerException("Unable to load library version file", e);
        }
    }
}
