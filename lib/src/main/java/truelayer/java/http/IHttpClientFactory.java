package truelayer.java.http;

import retrofit2.Retrofit;

public interface IHttpClientFactory {
    Retrofit create(String baseUrl);
}
