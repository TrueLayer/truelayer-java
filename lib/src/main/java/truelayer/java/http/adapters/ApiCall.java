package truelayer.java.http.adapters;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import truelayer.java.http.entities.ApiResponse;

/**
 * Meant to handle the conversion from a default Retrofit response
 * to a custom response object holding a data or error value.
 * @param <T> the type of response to handle
 */
@RequiredArgsConstructor
public class ApiCall<T> implements Call<ApiResponse<T>> {

    private final Call<T> delegate;

    @Override
    public Response execute() throws IOException {
        return handleResponse(delegate.execute());
    }

    @Override
    public void enqueue(Callback callback) {
        delegate.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callback.onResponse(call, handleResponse(response));
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onResponse(call, handleFailure(t));
            }
        });
    }

    @Override
    public boolean isExecuted() {
        return delegate.isExecuted();
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }

    @Override
    public boolean isCanceled() {
        return delegate.isCanceled();
    }

    @Override
    public Call clone() {
        return delegate.clone();
    }

    @Override
    public Request request() {
        return delegate.request();
    }

    @Override
    public Timeout timeout() {
        return delegate.timeout();
    }

    private Response handleFailure(Throwable throwable) {
        var apiResponse = ApiResponse.builder().error(ErrorMapper.fromThrowable(throwable));
        return Response.success(apiResponse);
    }

    private Response handleResponse(Response response) {
        if (response.isSuccessful()) {
            return Response.success(ApiResponse.builder().data(response.body()).build());
        }

        return Response.success(
                ApiResponse.builder().error(ErrorMapper.fromResponse(response)).build());
    }
}
