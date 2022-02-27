package truelayer.java.merchantaccounts.entities;

import java.util.List;
import lombok.Value;

@Value
public class ListMerchantAccountsResponse {
    private List<MerchantAccount> items;
}
