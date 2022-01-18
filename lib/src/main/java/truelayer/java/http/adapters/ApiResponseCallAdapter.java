package truelayer.java.http.adapters;

import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;
import truelayer.java.http.entities.ApiResponse;

/**
 * This class is required to properly override Retrofit defaults when deserializing
 * HTTP responses into objects.
 */
public class ApiResponseCallAdapter<T> implements CallAdapter<T, Call<ApiResponse<T>>> {

    private final Type responseType;

    public ApiResponseCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Call<ApiResponse<T>> adapt(Call<T> call) {
        return new ApiCall<>(call);
    }
}
