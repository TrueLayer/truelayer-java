package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import com.truelayer.java.payouts.entities.CreatePayoutResponse;
import com.truelayer.java.payouts.entities.Payout;
import com.truelayer.java.payouts.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.payouts.entities.beneficiary.Beneficiary;
import java.util.Collections;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("acceptance")
public class PayoutsAcceptanceTests extends AcceptanceTests {

    @Test
    @DisplayName("It should create a payout and get the details")
    @SneakyThrows
    public void shouldCreateAPayoutAndGetPayoutsDetails() {
        // find a merchant to execute the payout from
        MerchantAccount merchantAccount = getMerchantAccount(CurrencyCode.GBP);

        // create the payout
        CreatePayoutRequest createPayoutRequest = CreatePayoutRequest.builder()
                .merchantAccountId(merchantAccount.getId())
                .amountInMinor(RandomUtils.nextInt(10, 100))
                .currency(CurrencyCode.GBP)
                .beneficiary(Beneficiary.externalAccount()
                        .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                .accountNumber("00009650")
                                .sortCode("040668")
                                .build())
                        .reference("Java SDK payout test")
                        .accountHolderName("LucaB merchant")
                        .build())
                .metadata(Collections.singletonMap("a_custom_key", "a-custom-value"))
                .build();

        ApiResponse<CreatePayoutResponse> createPayoutResponse =
                tlClient.payouts().createPayout(createPayoutRequest).get();

        assertNotError(createPayoutResponse);

        String payoutId = createPayoutResponse.getData().getId();

        // get payout details
        ApiResponse<Payout> getPayoutResponse =
                tlClient.payouts().getPayout(payoutId).get();

        assertNotError(getPayoutResponse);
        assertEquals(payoutId, getPayoutResponse.getData().getId());
        assertEquals(merchantAccount.getId(), getPayoutResponse.getData().getMerchantAccountId());
    }
}
