package truelayer.java.http.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okio.Timeout;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;

import java.io.IOException;
import java.lang.reflect.Type;

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
