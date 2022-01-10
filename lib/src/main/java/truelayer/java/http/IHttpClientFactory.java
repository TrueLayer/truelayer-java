package truelayer.java.http;

import retrofit2.Retrofit;
import truelayer.java.VersionInfo;

public interface IHttpClientFactory {

    /**
     * Meant to create the internal Retrofit HTTP client
     * @param versionInfo DTO containing library version information
     * @param baseUrl the base url of all HTTP requests
     * @return a Retrofit HTTP client
     */
    Retrofit create(VersionInfo versionInfo, String baseUrl);
}
