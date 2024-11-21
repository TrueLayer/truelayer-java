package com.truelayer.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.truelayer.java.entities.ResourceType;
import java.net.URI;
import java.text.MessageFormat;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class HostedPaymentPageLinkBuilderTests {

    @Test
    @DisplayName("It should throw an exception if resource_token is not set")
    public void itShouldThrowExceptionIfResourceTokenIsNotSet() {
        Environment environment = Environment.live();
        String resourceId = UUID.randomUUID().toString();

        HostedPaymentPageLinkBuilder sut = new HostedPaymentPageLinkBuilder(environment);

        Throwable error = assertThrows(
                TrueLayerException.class, () -> sut.resourceId(resourceId).build());
        assertEquals("resource_token must be set", error.getMessage());
    }

    @Test
    @DisplayName("It should throw an exception if return_uri is not set")
    public void itShouldThrowExceptionIfReturnUriIsNotSet() {
        Environment environment = Environment.live();
        String resourceId = UUID.randomUUID().toString();
        String resourceToken = UUID.randomUUID().toString();

        HostedPaymentPageLinkBuilder sut = new HostedPaymentPageLinkBuilder(environment);

        Throwable error = assertThrows(
                TrueLayerException.class,
                () -> sut.resourceId(resourceId).resourceToken(resourceToken).build());
        assertEquals("return_uri must be set", error.getMessage());
    }

    @Test
    @DisplayName("It should yield a payments HPP link by default")
    public void itShouldYieldAPaymentsHppLinkByDefault() {
        Environment environment = Environment.live();
        String resourceId = UUID.randomUUID().toString();
        String resourceToken = UUID.randomUUID().toString();
        URI returnUri = URI.create("https://example.com");

        URI uri = new HostedPaymentPageLinkBuilder(environment)
                .resourceId(resourceId)
                .resourceToken(resourceToken)
                .returnUri(returnUri)
                .build();

        assertEquals(
                MessageFormat.format(
                        "{0}/payments#payment_id={1}&resource_token={2}&return_uri={3}",
                        environment.getHppUri().toString(), resourceId, resourceToken, returnUri),
                uri.toString());
    }

    @ParameterizedTest
    @DisplayName("It should yield an HPP link for a resource of type")
    @EnumSource(ResourceType.class)
    public void itShouldYieldAnHppLink(ResourceType resourceType) {
        Environment environment = Environment.live();
        String resourceId = UUID.randomUUID().toString();
        String resourceToken = UUID.randomUUID().toString();
        URI returnUri = URI.create("https://example.com");

        URI uri = new HostedPaymentPageLinkBuilder(environment)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .resourceToken(resourceToken)
                .returnUri(returnUri)
                .build();

        assertEquals(
                MessageFormat.format(
                        "{0}/{1}#{2}={3}&resource_token={4}&return_uri={5}",
                        environment.getHppUri().toString(),
                        resourceType.getHppLinkPath(),
                        resourceType.getHppLinkQueryParameter(),
                        resourceId,
                        resourceToken,
                        returnUri),
                uri.toString());
    }

    @Test
    @DisplayName("It should yield an HPP link with extra optional parameters")
    public void itShouldYieldAPaymentsHppLinkWithExtraOptionalParameters() {
        Environment environment = Environment.live();
        String resourceId = UUID.randomUUID().toString();
        String resourceToken = UUID.randomUUID().toString();
        URI returnUri = URI.create("https://example.com");
        int maxWaitSeconds = 15;
        boolean signup = true;

        URI uri = new HostedPaymentPageLinkBuilder(environment)
                .resourceId(resourceId)
                .resourceToken(resourceToken)
                .returnUri(returnUri)
                .maxWaitForResultSeconds(maxWaitSeconds)
                .signup(signup)
                .build();

        assertEquals(
                MessageFormat.format(
                        "{0}/payments#payment_id={1}&resource_token={2}&return_uri={3}&max_wait_for_result={4}&signup={5}",
                        environment.getHppUri().toString(),
                        resourceId,
                        resourceToken,
                        returnUri,
                        maxWaitSeconds,
                        signup),
                uri.toString());
    }
}
