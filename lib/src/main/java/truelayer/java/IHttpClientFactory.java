package truelayer.java;

import retrofit2.Retrofit;

public interface IHttpClientFactory {
    Retrofit create(String baseUrl);
}
