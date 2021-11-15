package truelayer.java.http;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import truelayer.java.TrueLayerException;

import java.io.IOException;

public class ErrorHandlerInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException{

        Response response;
        try{
            response = chain.proceed(chain.request());
        }catch (IOException exception){
            //todo avoid sending original messages as they may leak sensible info
            throw new TrueLayerException(exception.getMessage());
        }
        if(!response.isSuccessful() && !response.isRedirect()){
            throw new TrueLayerException(response.body() != null ? response.body().string() : "");
        }

        return response;
    }
}
