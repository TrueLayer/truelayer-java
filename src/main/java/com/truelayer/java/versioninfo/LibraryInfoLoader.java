package com.truelayer.java.versioninfo;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Class the loads the version of the library during the client initialization.
 * This component leverages a gradle build steps that prepares a truelayer-java.version.properties
 * file in project's resources directory.
 */
public class LibraryInfoLoader {
    private static final String CONFIG_FILE_PREXIF = "truelayer-java";

    public VersionInfo load() {
        PropertiesConfiguration libraryVersionProps = getVersionInfoPropertiesFile();

        return VersionInfo.builder()
                .libraryName(libraryVersionProps.getString(Constants.VersionInfo.NAME))
                .libraryVersion(libraryVersionProps.getString(Constants.VersionInfo.VERSION))
                .build();
    }

    private PropertiesConfiguration getVersionInfoPropertiesFile() {
        try {
            return new Configurations().properties(CONFIG_FILE_PREXIF + "." + "version" + ".properties");
        } catch (ConfigurationException e) {
            throw new TrueLayerException("Unable to load library version file", e);
        }
    }
}
