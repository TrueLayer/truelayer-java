package com.truelayer.java.payouts.entities.beneficiary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateFlexibleDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();

        // Try to parse as date first (yyyy-MM-dd)
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // If that fails, try to parse as timestamp and extract the date part
            try {
                return LocalDate.parse(dateString, TIMESTAMP_FORMATTER);
            } catch (DateTimeParseException ex) {
                // If both fail, return epoch date as default, as this is not critical
                return LocalDate.ofEpochDay(0);
            }
        }
    }
}
