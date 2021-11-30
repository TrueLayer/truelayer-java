package truelayer.java.http;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import truelayer.java.http.ApiResponseCallAdapter.ApiCall;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ApiResponseCallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != ApiCall.class) {
            return null;
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType != ApiResponse.class) {
            throw new IllegalArgumentException("type must be of type ApiResponse");
        }

        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new ApiResponseCallAdapter<>(bodyType);
    }
}
