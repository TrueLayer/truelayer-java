package com.truelayer.java.contract;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGeneratorUserOptions;
import com.atlassian.ta.wiremockpactgenerator.pactgenerator.PactGeneratorRegistry;
import com.atlassian.ta.wiremockpactgenerator.pactgenerator.PactGeneratorRequest;
import com.atlassian.ta.wiremockpactgenerator.pactgenerator.PactGeneratorResponse;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.LoggedResponse;
import com.github.tomakehurst.wiremock.http.MultiValue;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;

@Builder
public class WiremockPactExtension extends PostServeAction {
    WireMockPactGeneratorUserOptions userOptions;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void doGlobalAction(ServeEvent serveEvent, Admin admin) {
        LoggedRequest request = serveEvent.getRequest();
        LoggedResponse response = serveEvent.getResponse();

        final PactGeneratorRequest.Builder requestBuilder = new PactGeneratorRequest.Builder()
                .withMethod(request.getMethod().value())
                .withUrl(request.getUrl())
                .withHeaders(extractHeaders(request.getHeaders()))
                .withBody(request.getBodyAsString());

        final PactGeneratorResponse.Builder responseBuilder = new PactGeneratorResponse.Builder()
                .withStatus(response.getStatus())
                .withHeaders(extractHeaders(response.getHeaders()))
                .withIsConfiguredResponse(true) // todo: no idea what this means. review
                .withBody(response.getBody() == null ? "" : response.getBodyAsString());

        PactGeneratorRegistry.processInteraction(
                ObjectUtils.isNotEmpty(userOptions)
                        ? userOptions
                        :
                        // default user options
                        new WireMockPactGeneratorUserOptions(
                                "JavaSDK",
                                "PaymentsV3",
                                Collections.emptyList(),
                                Collections.emptyList(),
                                true,
                                Collections.emptyList(),
                                Collections.emptyList()),
                requestBuilder.build(),
                responseBuilder.build());
    }

    private Map<String, List<String>> extractHeaders(final HttpHeaders wireMockHeaders) {
        return wireMockHeaders.all().stream().collect(Collectors.toMap(MultiValue::key, MultiValue::values));
    }
}
