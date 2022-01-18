package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.SUCCEEDED;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
public class Succeeded extends BasePaymentDetail {
    private String id;

    private int amountInMinor;

    private String currency;

    private BaseBeneficiary beneficiary;

    private User user;

    private BasePaymentMethod paymentMethod;

    private String createdAt;

    private Optional<AuthorizationFlow> authorizationFlow;

    private SourceOfFunds sourceOfFunds;

    private String succeededAt;

    private final Status status = SUCCEEDED;
}
