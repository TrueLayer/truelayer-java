package com.truelayer.java.http;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.mappers.ErrorMapper;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import retrofit2.*;

/**
 * This class is extends <a href="https://github.com/square/retrofit/blob/master/retrofit-adapters/java8/src/main/java/retrofit2/adapter/java8/Java8CallAdapterFactory.java">Retrofit's ResponseCallAdapter</a>
 * to support TrueLayer ApiResponse wrapper object.
 *
 * @see ApiResponse
 */
final class TrueLayerResponseCallAdapter<R> implements CallAdapter<R, CompletableFuture<ApiResponse<R>>> {
    private final Type responseType;
    private final ErrorMapper errorMapper;

    TrueLayerResponseCallAdapter(Type responseType) {
        this.responseType = responseType;
        this.errorMapper = new ErrorMapper();
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

    protected class ResponseCallback implements Callback<R> {
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

    private static final class CallCancelCompletableFuture<T> extends CompletableFuture<T> {
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

    private ApiResponse<R> handleResponse(Response<R> response) {
        if (response.isSuccessful()) {
            return ApiResponse.<R>builder().data(response.body()).build();
        }

        return ApiResponse.<R>builder()
                .error(errorMapper.toProblemDetails(response))
                .build();
    }
}
