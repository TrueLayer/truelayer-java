package truelayer.java.acceptance;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import truelayer.java.ClientCredentials;
import truelayer.java.Environment;
import truelayer.java.SigningOptions;
import truelayer.java.TrueLayerClient;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.merchantaccounts.entities.MerchantAccount;

@Tag("acceptance")
public abstract class AcceptanceTests {

    protected static TrueLayerClient tlClient;

    private MerchantAccount merchantAccount;

    @BeforeAll
    public static void setup() {
        tlClient = TrueLayerClient.New()
                .environment(Environment.sandbox())
                .clientCredentials(ClientCredentials.builder()
                        .clientId(System.getenv("TL_CLIENT_ID"))
                        .clientSecret(System.getenv("TL_CLIENT_SECRET"))
                        .build())
                .signingOptions(SigningOptions.builder()
                        .keyId(System.getenv("TL_SIGNING_KEY_ID"))
                        .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY").getBytes(StandardCharsets.UTF_8))
                        .build())
                .withHttpLogs()
                .build();
    }

    /**
     * Internal utility to save some API call
     */
    @SneakyThrows
    @Synchronized
    protected MerchantAccount getMerchantAccount() {
        if (isNotEmpty(merchantAccount)) {
            return merchantAccount;
        }

        merchantAccount = tlClient.merchantAccounts().listMerchantAccounts().get().getData().getItems().stream()
                .filter(m -> m.getCurrency().equals(CurrencyCode.GBP))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("test merchant account not found"));

        return merchantAccount;
    }
}
