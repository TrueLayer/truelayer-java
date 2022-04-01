package com.truelayer.java;

import static com.truelayer.java.TestUtils.*;
import static com.truelayer.java.Utils.getObjectMapper;

import com.truelayer.java.payments.entities.paymentdetail.SettledPaymentDetail;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for utilities
 */
public class UtilsTests {

    @SneakyThrows
    @Test
    @DisplayName("It should deserialize a payment with an unknown response field")
    public void itShouldDeserializeAPaymentWithUnknownResponseField() {
        Assertions.assertDoesNotThrow(() -> getObjectMapper()
                .readValue(
                        Files.readAllBytes(Paths.get(JSON_RESPONSES_LOCATION
                                + "payments/200.get_payment_by_id.unknown_response_field.json")),
                        SettledPaymentDetail.class));
    }
}
