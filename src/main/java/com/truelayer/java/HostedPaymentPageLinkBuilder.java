package com.truelayer.java;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.entities.ResourceType;
import java.net.URI;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class HostedPaymentPageLinkBuilder {
    private final Environment environment;

    private ResourceType resourceType = ResourceType.PAYMENT;
    private String resourceId;
    private String resourceToken;
    private URI returnUri;
    private Integer maxWaitForResultSeconds;
    private Boolean signup;

    public HostedPaymentPageLinkBuilder resourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public HostedPaymentPageLinkBuilder resourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public HostedPaymentPageLinkBuilder resourceToken(String resourceToken) {
        this.resourceToken = resourceToken;
        return this;
    }

    public HostedPaymentPageLinkBuilder returnUri(URI returnUri) {
        this.returnUri = returnUri;
        return this;
    }

    public HostedPaymentPageLinkBuilder maxWaitForResultSeconds(int maxWaitForResultSeconds) {
        this.maxWaitForResultSeconds = maxWaitForResultSeconds;
        return this;
    }

    public HostedPaymentPageLinkBuilder signup(boolean signup) {
        this.signup = signup;
        return this;
    }

    public URI build() {
        if (isEmpty(this.resourceId)) {
            throw new TrueLayerException("resource_id must be set");
        }

        if (isEmpty(resourceToken)) {
            throw new TrueLayerException("resource_token must be set");
        }

        if (isEmpty(returnUri) || isEmpty(returnUri.toString())) {
            throw new TrueLayerException("return_uri must be set");
        }

        URI hppLink = URI.create(MessageFormat.format(
                "{0}/{1}#{2}={3}&resource_token={4}&return_uri={5}",
                environment.getHppUri(),
                resourceType.getHppLinkPath(),
                resourceType.getHppLinkQueryParameter(),
                resourceId,
                resourceToken,
                returnUri));

        if (!isEmpty(maxWaitForResultSeconds)) {
            hppLink = URI.create(MessageFormat.format("{0}&max_wait_for_result={1}", hppLink, maxWaitForResultSeconds));
        }

        if (!isEmpty(signup)) {
            hppLink = URI.create(MessageFormat.format("{0}&signup={1}", hppLink, signup));
        }

        return hppLink;
    }
}
