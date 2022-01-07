package truelayer.java.http;

public interface IApiCallback<T> {
    void onResponse(T response);
}
