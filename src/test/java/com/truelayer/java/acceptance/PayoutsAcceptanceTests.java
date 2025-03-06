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
import com.truelayer.java.payouts.entities.schemeselection.SchemeSelection;
import com.truelayer.java.payouts.entities.submerchants.BusinessClient;
import com.truelayer.java.payouts.entities.submerchants.SubMerchants;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("acceptance")
public class PayoutsAcceptanceTests extends AcceptanceTests {

    @ParameterizedTest(name = "It should create a {0} payout and get the details")
    @MethodSource("providePayoutsArgumentsForDifferentCurrencies")
    @SneakyThrows
    public void shouldCreateAPayoutAndGetPayoutsDetails(
            CurrencyCode currencyCode, AccountIdentifier accountIdentifier, SchemeSelection schemeSelection) {
        // find a merchant to execute the payout from
        MerchantAccount merchantAccount = getMerchantAccount(currencyCode);

        // create the payout
        CreatePayoutRequest createPayoutRequest = CreatePayoutRequest.builder()
                .merchantAccountId(merchantAccount.getId())
                .amountInMinor(ThreadLocalRandom.current().nextInt(10, 100))
                .currency(currencyCode)
                .beneficiary(Beneficiary.externalAccount()
                        .accountIdentifier(accountIdentifier)
                        .reference(String.format("Java SDK %s payout test", currencyCode.name()))
                        .accountHolderName("LucaB merchant")
                        .build())
                .schemeSelection(schemeSelection)
                .subMerchants(SubMerchants.builder()
                        .ultimateCounterparty(BusinessClient.businessClient()
                                .tradingName("A sub merchant trading name")
                                .registrationNumber("123456")
                                .build())
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

    public static Stream<Arguments> providePayoutsArgumentsForDifferentCurrencies() {
        return Stream.of(
                Arguments.of(
                        CurrencyCode.GBP,
                        AccountIdentifier.sortCodeAccountNumber()
                                .accountNumber("00009650")
                                .sortCode("040668")
                                .build(),
                        SchemeSelection.instantPreferred().build()),
                Arguments.of(
                        CurrencyCode.PLN,
                        AccountIdentifier.iban().iban("GB25CLRB04066800046876").build(),
                        SchemeSelection.preselected()
                                .schemeId("polish_domestic_express")
                                .build()));
    }
}
