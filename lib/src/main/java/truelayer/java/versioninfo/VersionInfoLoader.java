package truelayer.java.versioninfo;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import truelayer.java.TrueLayerException;
import truelayer.java.common.Constants;

/**
 * Class the loads the version of the library during the client initialization.
 * This component leverages a gradle build steps that prepares a truelayer-java.version.properties
 * file in project's resources directory.
 */
public class VersionInfoLoader {
    private static final String CONFIG_FILE_PREXIF = "truelayer-java";

    public VersionInfo load() {
        PropertiesConfiguration versionProps = getVersionInfoPropertiesFile();

        return VersionInfo.builder()
                .libraryName(versionProps.getString(Constants.VersionInfo.NAME))
                .libraryVersion(versionProps.getString(Constants.VersionInfo.VERSION))
                .build();
    }

    private PropertiesConfiguration getVersionInfoPropertiesFile() {
        try {
            return new Configurations().properties(CONFIG_FILE_PREXIF + "." + "version" + ".properties");
        } catch (ConfigurationException e) {
            throw new TrueLayerException(String.format("Unable to load %s", "version"), e);
        }
    }
}
