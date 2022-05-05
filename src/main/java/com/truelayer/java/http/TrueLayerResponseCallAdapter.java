package com.truelayer.java.http;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.mappers.ErrorMapper;
import java.lang.reflect.Type;
import lombok.SneakyThrows;
import retrofit2.*;

/**
 * This class is extends <a href="https://github.com/square/retrofit/blob/master/retrofit-adapters/java8/src/main/java/retrofit2/adapter/java8/Java8CallAdapterFactory.java">Retrofit's ResponseCallAdapter</a>
 * to support TrueLayer ApiResponse wrapper object.
 *
 * @see ApiResponse
 */
final class TrueLayerResponseCallAdapter<R> implements CallAdapter<R, ApiResponse<R>> {
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

    @SneakyThrows
    @Override
    public ApiResponse<R> adapt(final Call<R> call) {
        Response<R> response = call.execute();

        if (response.isSuccessful()) {
            return ApiResponse.<R>builder().data(response.body()).build();
        }

        return ApiResponse.<R>builder()
                .error(errorMapper.toProblemDetails(response))
                .build();
    }
}
