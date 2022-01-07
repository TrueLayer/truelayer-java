package truelayer.java.http;

import lombok.RequiredArgsConstructor;
import truelayer.java.http.adapters.ApiCall;

@RequiredArgsConstructor
public class CancellableTask implements ICancellableTask{

    private final ApiCall apiCall;

    @Override
    public void cancel() {
        this.apiCall.cancel();
    }
}
