package truelayer.java;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Configuration {
    private final static String AUTH_CONFIG_PREFIX = "tl.auth.";

    private final String clientId;
    private final String clientSecret;
    private final String scope;
    private final String tokenEndpoint;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public String getGrantType() {
        return grantType;
    }

    private final String grantType;

    public Configuration() throws ConfigurationException {
        //todo: review implementation
        var properties = new Configurations().properties("application.properties");

        this.clientId = properties.getString(AUTH_CONFIG_PREFIX + "client.id");
        this.clientSecret = properties.getString(AUTH_CONFIG_PREFIX + "client.secret");
        this.scope = properties.getString(AUTH_CONFIG_PREFIX + "scope"); //todo: this should accept multiple scopes
        this.grantType = properties.getString(AUTH_CONFIG_PREFIX + "grant.type");
        this.tokenEndpoint = properties.getString(AUTH_CONFIG_PREFIX + "token.endpoint");
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
}
