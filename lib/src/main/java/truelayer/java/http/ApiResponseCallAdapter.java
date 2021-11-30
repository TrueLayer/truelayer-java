package truelayer.java.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.Request;
import okio.Timeout;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

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

    @RequiredArgsConstructor
    public static class ApiCall<T> implements Call<ApiResponse<T>>{

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

        private Response handleFailure(Throwable throwable){
            var apiResponse = new ApiResponse<>(null, ApiResponse.ProblemDetails.builder()
                    .title(throwable.getMessage())
                    .detail(throwable.getLocalizedMessage())
                    .build());
            return Response.success(apiResponse);
        }

        @SneakyThrows //todo review
        private Response handleResponse(Response response){
            if(response.isSuccessful()){
                return Response.success(
                        new ApiResponse<>(response.body(),null)
                );
            }

            //todo leverage a singleton mapper
            var errorBodyString = response.errorBody().string();
            var problemDetails = new ObjectMapper().readValue(errorBodyString, ApiResponse.ProblemDetails.class);

            //todo improve fallbacks
            if(StringUtils.isEmpty(problemDetails.getTitle())){
                problemDetails = ApiResponse.ProblemDetails.builder()
                        .title(errorBodyString)
                        .type(errorBodyString)
                        .detail(errorBodyString)
                        .build();
            }

            return Response.success(
                    new ApiResponse<>(null, problemDetails)
            );
        }
    }
}
