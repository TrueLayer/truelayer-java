package truelayer.java.payments.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SubmitProviderSelectionRequest {
    private String providerId;
}
