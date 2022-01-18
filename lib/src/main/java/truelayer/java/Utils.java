package truelayer.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class Utils {
    private Utils() {}

    private static ObjectMapper OBJECT_MAPPER_INSTANCE = null;

    public static ObjectMapper getObjectMapper(){
        if(OBJECT_MAPPER_INSTANCE == null){
            var objectMapper = new ObjectMapper();

            // required for optionals deserialization
            objectMapper.registerModule(new Jdk8Module());
            // serialize all camel cases fields to snake
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            OBJECT_MAPPER_INSTANCE = objectMapper;
        }
        return OBJECT_MAPPER_INSTANCE;
    }
}
