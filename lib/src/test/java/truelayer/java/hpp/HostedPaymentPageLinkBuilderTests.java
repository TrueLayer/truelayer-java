package truelayer.java.hpp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static truelayer.java.TestUtils.A_HPP_ENDPOINT;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerException;

class HostedPaymentPageLinkBuilderTests {

    public static final String A_RETURN_URI = "https://a-redirect-uri.com";
    public static final String A_PAYMENT_TOKEN = "a-payment-token";
    public static final String A_PAYMENT_ID = "a-payment-id";

    @Test
    @DisplayName("it should yield an HPP link with the given details")
    public void itShouldYieldAnHppLink() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        URI link = sut.getHostedPaymentPageLink(A_PAYMENT_ID, A_PAYMENT_TOKEN, URI.create(A_RETURN_URI));

        assertEquals(
                new StringBuilder(A_HPP_ENDPOINT)
                        .append("/payments#payment_id=")
                        .append(A_PAYMENT_ID)
                        .append("&payment_token=")
                        .append(A_PAYMENT_TOKEN)
                        .append("&return_uri=")
                        .append(A_RETURN_URI)
                        .toString(),
                link.toString());
    }

    @Test
    @DisplayName("it should thrown an exception if redirect_uri is empty")
    public void itShouldThrowExceptionForEmptyRedirectUrl() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> sut.getHostedPaymentPageLink(A_PAYMENT_ID, A_PAYMENT_TOKEN, URI.create("")));

        assertEquals("return_uri must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("it should thrown an exception if payment_id is empty")
    public void itShouldThrowExceptionForEmptyPaymentId() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> sut.getHostedPaymentPageLink("", A_PAYMENT_TOKEN, URI.create(A_RETURN_URI)));

        assertEquals("payment_id must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("it should thrown an exception if payment_token is empty")
    public void itShouldThrowExceptionForEmptyResourceToken() {
        IHostedPaymentPageLinkBuilder sut = buildHppBuilder();

        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> sut.getHostedPaymentPageLink(A_PAYMENT_ID, "", URI.create(A_RETURN_URI)));

        assertEquals("payment_token must be set", thrown.getMessage());
    }

    private HostedPaymentPageLinkBuilder buildHppBuilder() {
        return HostedPaymentPageLinkBuilder.New().endpoint(A_HPP_ENDPOINT).build();
    }
}
