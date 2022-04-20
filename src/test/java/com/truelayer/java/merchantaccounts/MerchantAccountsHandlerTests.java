package com.truelayer.java.merchantaccounts;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.merchantaccounts.entities.ListPaymentSourcesQuery;
import com.truelayer.java.merchantaccounts.entities.ListTransactionsQuery;
import com.truelayer.java.merchantaccounts.entities.UpdateSweepingRequest;
import com.truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MerchantAccountsHandlerTests {

    private static String A_MERCHANT_ACCOUNT_ID = "a-merchant-account-id";

    @Test
    @DisplayName("It should call the list merchant accounts endpoint")
    public void shouldCallListMerchantAccountsEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.listMerchantAccounts();

        verify(merchantsApi, times(1)).listMerchantAccounts();
    }

    @Test
    @DisplayName("It should call the get merchant account by id endpoint")
    public void shouldCallGetMerchantAccountByIdEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.getMerchantAccountById(A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).getMerchantAccountById(A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the list transactions endpoint")
    public void shouldCallListTransactionsEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        String fromStr = "2021-02-20T06:30:00Z";
        String toStr = "2022-02-20T06:30:00Z";
        ListTransactionsQuery query = ListTransactionsQuery.builder()
                .from(ZonedDateTime.parse(fromStr))
                .to(ZonedDateTime.parse(toStr))
                .type(TransactionTypeQuery.PAYOUT)
                .build();

        sut.listTransactions(A_MERCHANT_ACCOUNT_ID, query);

        verify(merchantsApi, times(1)).listTransactions(A_MERCHANT_ACCOUNT_ID, fromStr, toStr, query.type());
    }

    @Test
    @DisplayName("It should call the update sweeping endpoint")
    public void shouldCallUpdateSweepingEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        UpdateSweepingRequest request =
                UpdateSweepingRequest.builder().currency(CurrencyCode.GBP).build();

        sut.updateSweeping(A_MERCHANT_ACCOUNT_ID, request);

        verify(merchantsApi, times(1)).updateSweeping(A_MERCHANT_ACCOUNT_ID, request);
    }

    @Test
    @DisplayName("It should call the get sweeping settings endpoint")
    public void shouldCallGetSweepingSettingsEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.getSweepingSettings(A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).getSweepingSettings(A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint")
    public void shouldCallDisableSweepingEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.disableSweeping(A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).disableSweeping(A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint")
    public void shouldCallListPaymentSourcesEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        ListPaymentSourcesQuery query =
                ListPaymentSourcesQuery.builder().userId("a-user-id").build();

        sut.listPaymentSources(A_MERCHANT_ACCOUNT_ID, query);

        verify(merchantsApi, times(1)).listPaymentSources(A_MERCHANT_ACCOUNT_ID, query.userId());
    }
}
