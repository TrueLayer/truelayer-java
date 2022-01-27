package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = SupportedRedirectStatus.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SupportedRedirectStatus.class, name = "supported"),
    @JsonSubTypes.Type(value = NotSupportedRedirectStatus.class, name = "not_supported"),
})
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class RedirectStatus {}
