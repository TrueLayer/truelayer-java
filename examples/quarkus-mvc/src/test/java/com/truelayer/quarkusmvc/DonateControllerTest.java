package com.truelayer.quarkusmvc;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DonateControllerTest {
    @Test
    void testDonateEndpoint() {
        given().when().get("/donations").then().statusCode(200);
    }
}
