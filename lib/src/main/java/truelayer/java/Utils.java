package truelayer.java;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

/**
 * Library constants class
 */
public class Utils {
    private Utils() {}

    private static ObjectMapper OBJECT_MAPPER_INSTANCE = null;

    public static ObjectMapper getObjectMapper() {
        if (OBJECT_MAPPER_INSTANCE == null) {
            ObjectMapper objectMapper = new ObjectMapper();

            // required for optionals deserialization
            objectMapper.registerModule(new Jdk8Module());
            // serialize all camel cases fields to snake
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            // do not include null fields in JSON
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            OBJECT_MAPPER_INSTANCE = objectMapper;
        }
        return OBJECT_MAPPER_INSTANCE;
    }

    public static <T> void validateObject(T object) {
        final Validator validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) {
            String validationError = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new TrueLayerException(validationError);
        }
    }
}
