package truelayer.java.acceptance;

import static truelayer.java.TestUtils.assertNotError;

import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

public class AuthAcceptanceTests extends AcceptanceTests {

    @SneakyThrows
    @Test
    @DisplayName("It should get an access token")
    public void itShouldGetAnAccessToken() {
        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList("payments"))
                .get();

        assertNotError(response);
    }
}
