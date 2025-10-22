package com.truelayer.java.payouts.entities.beneficiary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocalDateFlexibleDeserializerTests {

    private final LocalDateFlexibleDeserializer deserializer = new LocalDateFlexibleDeserializer();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("It should deserialize a date in yyyy-MM-dd format")
    public void shouldDeserializeDateFormat() throws IOException {
        String json = "\"2025-10-15\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        parser.nextToken();

        LocalDate result = deserializer.deserialize(parser, null);

        assertEquals(LocalDate.of(2025, 10, 15), result);
    }

    @Test
    @DisplayName("It should deserialize a timestamp in yyyy-MM-dd'T'HH:mm:ssXXX format")
    public void shouldDeserializeTimestampFormat() throws IOException {
        String json = "\"2025-10-15T00:00:00+00:00\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        parser.nextToken();

        LocalDate result = deserializer.deserialize(parser, null);

        assertEquals(LocalDate.of(2025, 10, 15), result);
    }

    @Test
    @DisplayName("It should deserialize a timestamp with different timezone")
    public void shouldDeserializeTimestampWithTimezone() throws IOException {
        String json = "\"2025-10-15T14:30:00+01:00\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        parser.nextToken();

        LocalDate result = deserializer.deserialize(parser, null);

        assertEquals(LocalDate.of(2025, 10, 15), result);
    }

    @Test
    @DisplayName("It should return epoch date when given an invalid format")
    public void shouldReturnEpochDateForInvalidFormat() throws IOException {
        String json = "\"invalid-date-string\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        parser.nextToken();

        LocalDate result = deserializer.deserialize(parser, null);

        assertEquals(LocalDate.ofEpochDay(0), result);
    }

    @Test
    @DisplayName("It should return epoch date when given an empty string")
    public void shouldReturnEpochDateForEmptyString() throws IOException {
        String json = "\"\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        parser.nextToken();

        LocalDate result = deserializer.deserialize(parser, null);

        assertEquals(LocalDate.ofEpochDay(0), result);
    }
}
