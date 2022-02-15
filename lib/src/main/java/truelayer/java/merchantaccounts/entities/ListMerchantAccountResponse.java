package truelayer.java.merchantaccounts.entities;

import java.util.List;
import lombok.Value;

@Value
public class ListMerchantAccountResponse {
    List<MerchantAccount> items;
}
