package com.truelayer.java.hpp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HostedPaymentPageLinkBuilderTests {

    public static final String A_RETURN_URI = "https://a-redirect-uri.com";
    public static final String A_RESOURCE_TOKEN = "a-resource-token";
    public static final String A_PAYMENT_ID = "a-payment-id";

    @Test
    @DisplayName("It should yield an HPP link with the given details")
    public void itShouldYieldAnHppLink() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        URI link = sut.getHostedPaymentPageLink(A_PAYMENT_ID, A_RESOURCE_TOKEN, URI.create(A_RETURN_URI));

        assertEquals(
                TestUtils.A_HPP_ENDPOINT + "/payments#payment_id="
                        + A_PAYMENT_ID
                        + "&resource_token="
                        + A_RESOURCE_TOKEN
                        + "&return_uri="
                        + A_RETURN_URI,
                link.toString());
    }

    @Test
    @DisplayName("It should thrown an exception if redirect_uri is empty")
    public void itShouldThrowExceptionForEmptyRedirectUrl() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> sut.getHostedPaymentPageLink(A_PAYMENT_ID, A_RESOURCE_TOKEN, URI.create("")));

        assertEquals("return_uri must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should thrown an exception if payment_id is empty")
    public void itShouldThrowExceptionForEmptyPaymentId() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> sut.getHostedPaymentPageLink("", A_RESOURCE_TOKEN, URI.create(A_RETURN_URI)));

        assertEquals("payment_id must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should thrown an exception if resource_token is empty")
    public void itShouldThrowExceptionForEmptyResourceToken() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> sut.getHostedPaymentPageLink(A_PAYMENT_ID, "", URI.create(A_RETURN_URI)));

        assertEquals("resource_token must be set", thrown.getMessage());
    }

    private HostedPaymentPageLinkBuilder buildHppBuilder() {
        return HostedPaymentPageLinkBuilder.New().uri(TestUtils.A_HPP_ENDPOINT).build();
    }
}
