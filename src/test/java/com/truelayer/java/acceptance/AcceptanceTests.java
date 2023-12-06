package com.truelayer.java.acceptance;

import static com.truelayer.java.Constants.Scopes.*;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.Environment;
import com.truelayer.java.SigningOptions;
import com.truelayer.java.TrueLayerClient;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

@Tag("acceptance")
public abstract class AcceptanceTests {

    protected static Environment environment;

    protected static TrueLayerClient tlClient;

    private final Map<CurrencyCode, MerchantAccount> merchantAccounts = new HashMap<>();

    @BeforeAll
    public static void setup() {
        environment = Environment.development();
        tlClient = TrueLayerClient.New()
                .environment(environment)
                .clientCredentials(ClientCredentials.builder()
                        .clientId(System.getenv("TL_CLIENT_ID_DEV"))
                        .clientSecret(System.getenv("TL_CLIENT_SECRET_DEV"))
                        .build())
                .signingOptions(SigningOptions.builder()
                        .keyId(System.getenv("TL_SIGNING_KEY_ID_DEV"))
                        .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY_DEV").getBytes(StandardCharsets.UTF_8))
                        .build())
                .withHttpLogs()
                .withCredentialsCaching()
                .build();
    }

    /**
     * Internal utility to save some API call
     */
    @SneakyThrows
    @Synchronized
    protected MerchantAccount getMerchantAccount(CurrencyCode currencyCode) {
        if (merchantAccounts.containsKey(currencyCode)) {
            return merchantAccounts.get(currencyCode);
        }

        MerchantAccount merchantAccount =
                tlClient.merchantAccounts().listMerchantAccounts().get().getData().getItems().stream()
                        .filter(m -> m.getCurrency().equals(currencyCode))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("test merchant account not found"));

        merchantAccounts.put(currencyCode, merchantAccount);

        return merchantAccount;
    }
}
