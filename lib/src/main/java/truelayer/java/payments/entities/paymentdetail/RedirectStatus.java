package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = SupportedRedirectStatus.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SupportedRedirectStatus.class, name = "supported"),
    @JsonSubTypes.Type(value = NotSupportedRedirectStatus.class, name = "not_supported"),
})
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class RedirectStatus {}
