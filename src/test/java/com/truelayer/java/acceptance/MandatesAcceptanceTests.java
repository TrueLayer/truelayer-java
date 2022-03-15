package com.truelayer.java.acceptance;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.recurringpayments.entities.CreateMandateRequest;
import com.truelayer.java.recurringpayments.entities.CreateMandateResponse;
import com.truelayer.java.recurringpayments.entities.mandate.Constraints;
import com.truelayer.java.recurringpayments.entities.mandate.Mandate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("acceptance")
public class MandatesAcceptanceTests extends AcceptanceTests {

    @Test
    @DisplayName("It should create a mandate")
    @SneakyThrows
    public void itShouldCreateAMandate() {
        // create payment
        CreateMandateRequest createMandateRequest = CreateMandateRequest.builder()
                .mandate(Mandate.vrpSweepingMandate()
                        .beneficiary(Beneficiary.externalAccount()
                                .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                        .accountNumber("10003957")
                                        .sortCode("140662")
                                        .build())
                                .accountHolderName("Andrea Java SDK")
                                .reference("a reference")
                                .build())
                        .build())
                .currency(CurrencyCode.GBP)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .name("John Smith")
                        .email("john@truelayer.com")
                        .build())
                .constraints(Constraints.builder()
                        .validFrom(LocalDateTime.now().plusDays(5).format(DateTimeFormatter.ISO_DATE_TIME))
                        .validTo(LocalDateTime.now().plusDays(25).format(DateTimeFormatter.ISO_DATE_TIME))
                        .maximumIndividualAmount(100)
                        .build())
                .build();

        ApiResponse<CreateMandateResponse> createMandateResponse =
                tlClient.mandates().createMandate(createMandateRequest).get();

        assertNotError(createMandateResponse);
    }
}
