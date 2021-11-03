package truelayer.java.auth;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Configuration {
    private final static String AUTH_CONFIG_PREFIX = "tl.auth.";
    private final PropertiesConfiguration configuration;

    //todo temp
    public Configuration() throws ConfigurationException {
        Configurations configurations = new Configurations();
        this.configuration = configurations.properties("application.properties");
    }

    public String getClientSecret() {
        return configuration.getString(AUTH_CONFIG_PREFIX + "client.secret");
    }

    public String getClientId() {
        return configuration.getString(AUTH_CONFIG_PREFIX + "client.id");
    }
}
