package com.truelayer.java.http;

import com.truelayer.java.http.entities.ApiResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * This class is extends <a href="https://github.com/square/retrofit/blob/master/retrofit-adapters/java8/src/main/java/retrofit2/adapter/java8/Java8CallAdapterFactory.java">Retrofit's Java8CallAdapterFactory</a>
 * to support TrueLayer ApiResponse wrapper object.
 *
 * @see ApiResponse
 */
final class TrueLayerApiAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != ApiResponse.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("ApiResponse return type must be parameterized"
                    + " as ApiResponse<Foo> or ApiResponse<? extends Foo>");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);

        return new TrueLayerResponseCallAdapter<>(responseType);
    }
}
