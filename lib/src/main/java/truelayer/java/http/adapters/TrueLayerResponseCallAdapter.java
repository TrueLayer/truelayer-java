package truelayer.java.http.adapters;

import retrofit2.*;
import truelayer.java.http.entities.ApiResponse;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public final class TrueLayerResponseCallAdapter<R>
        implements CallAdapter<R, CompletableFuture<ApiResponse<R>>> {
    private final Type responseType;

    TrueLayerResponseCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public CompletableFuture<ApiResponse<R>> adapt(final Call<R> call) {
        CompletableFuture<ApiResponse<R>> future = new CallCancelCompletableFuture<>(call);
        call.enqueue(new ResponseCallback(future));
        return future;
    }

    private class ResponseCallback implements Callback<R> {
        private final CompletableFuture<ApiResponse<R>> future;

        public ResponseCallback(CompletableFuture<ApiResponse<R>> future) {
            this.future = future;
        }

        @Override
        public void onResponse(Call<R> call, Response<R> response) {
            future.complete(handleResponse(response));
        }

        @Override
        public void onFailure(Call<R> call, Throwable t) {
            future.completeExceptionally(t);
        }
    }

    private final class CallCancelCompletableFuture<T> extends CompletableFuture<T> {
        private final Call<?> call;

        CallCancelCompletableFuture(Call<?> call) {
            this.call = call;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (mayInterruptIfRunning) {
                call.cancel();
            }
            return super.cancel(mayInterruptIfRunning);
        }
    }

    private ApiResponse handleResponse(Response response) {
        if (response.isSuccessful()) {
                   return ApiResponse.builder()
                            .data(response.body())
                            .build();
        }

        return ApiResponse.builder()
                        .error(ErrorMapper.fromResponse(response))
                        .build();
    }
}
