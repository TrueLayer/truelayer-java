package truelayer.java.http.adapters.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties
public class LegacyError {
    String error;
}
