package truelayer.java.payments.entities.paymentmethod.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = UserSelectionProvider.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserSelectionProvider.class, name = "user_selection"),
    @JsonSubTypes.Type(value = PreselectedProvider.class, name = "preselected")
})
@ToString
@EqualsAndHashCode
public abstract class Provider {}
