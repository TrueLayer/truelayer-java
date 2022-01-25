package truelayer.java.configuration;

import static java.util.Collections.unmodifiableList;
import static truelayer.java.common.Constants.ConfigurationKeys.*;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import truelayer.java.TrueLayerException;
import truelayer.java.common.Constants;
import truelayer.java.configuration.Configuration.ConfigurationBuilder;

public class ConfigurationAssembler {
    private static final String CONFIG_FILE_PREXIF = "truelayer-java";

    private final boolean useSandbox;

    public ConfigurationAssembler(boolean useSandbox) {
        this.useSandbox = useSandbox;
    }

    public Configuration assemble() {
        ConfigurationBuilder configBuilder = Configuration.builder();

        PropertiesConfiguration versionProps = getPropertiesFile("version");

        configBuilder.versionInfo(Configuration.VersionInfo.builder()
                .libraryName(versionProps.getString(Constants.VersionInfo.NAME))
                .libraryVersion(versionProps.getString(Constants.VersionInfo.VERSION))
                .build());

        PropertiesConfiguration applicationProps = getPropertiesFile("lib");

        configBuilder.authentication(new Configuration.Endpoint(
                useSandbox
                        ? applicationProps.getString(AUTH_ENDPOINT_URL_SANDBOX)
                        : applicationProps.getString(AUTH_ENDPOINT_URL_LIVE)));

        configBuilder.hostedPaymentPage(new Configuration.Endpoint(
                useSandbox
                        ? applicationProps.getString(HPP_ENDPOINT_URL_SANDBOX)
                        : applicationProps.getString(HPP_ENDPOINT_URL_LIVE)));

        configBuilder.payments(Configuration.Payments.builder()
                .endpointUrl(
                        useSandbox
                                ? applicationProps.getString(PAYMENTS_ENDPOINT_URL_SANDBOX)
                                : applicationProps.getString(PAYMENTS_ENDPOINT_URL_LIVE))
                .scopes(unmodifiableList(applicationProps.getList(String.class, PAYMENTS_SCOPES)))
                .build());

        return configBuilder.build();
    }

    private PropertiesConfiguration getPropertiesFile(String configFileName) {
        try {
            return new Configurations()
                    .properties(new StringBuilder(CONFIG_FILE_PREXIF)
                            .append(".")
                            .append(configFileName)
                            .append(".properties")
                            .toString());
        } catch (ConfigurationException e) {
            throw new TrueLayerException(String.format("Unable to load %s", configFileName), e);
        }
    }
}
