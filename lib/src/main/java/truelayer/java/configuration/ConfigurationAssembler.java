package truelayer.java.configuration;

import static java.util.Collections.unmodifiableList;
import static truelayer.java.common.Constants.ConfigurationKeys.*;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import truelayer.java.TrueLayerException;
import truelayer.java.common.Constants;

public class ConfigurationAssembler {
    private final boolean useSandbox;

    public ConfigurationAssembler(boolean useSandbox) {
        this.useSandbox = useSandbox;
    }

    public Configuration assemble() {
        var configBuilder = Configuration.builder();

        PropertiesConfiguration versionProps = null;
        try {
            versionProps = new Configurations().properties("version.properties");
        } catch (Exception e) {
            new TrueLayerException("Unable to load version properties", e);
        }

        configBuilder.versionInfo(Configuration.VersionInfo.builder()
                .libraryName(versionProps.getString(Constants.VersionInfo.NAME))
                .libraryVersion(versionProps.getString(Constants.VersionInfo.VERSION))
                .build());

        PropertiesConfiguration applicationProps = null;
        try {
            applicationProps = new Configurations().properties("application.properties");
        } catch (Exception e) {
            new TrueLayerException("Unable to load application properties", e);
        }

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
}
