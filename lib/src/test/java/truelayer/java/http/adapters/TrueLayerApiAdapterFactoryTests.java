package truelayer.java.http.adapters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TestUtils;

class TrueLayerApiAdapterFactoryTests {

    @Test
    @Disabled
    @DisplayName("it should recognise a CompletableFuture<ApiResponse<T>> instance")
    public void shouldCatchACompletableFutureApiResponse() {
        var sut = new TrueLayerApiAdapterFactory();

        // todo complete
        var responseAdapter = sut.get(
                CompletableFuture.completedFuture(TestUtils.buildAccessToken()).getClass(), null, null);

        assertEquals(TrueLayerResponseCallAdapter.class, responseAdapter.getClass());
    }
}
