package it.jump3.service;

import it.jump3.config.ConfigService;
import it.jump3.rest.BitlyClient;
import it.jump3.rest.model.BitlyRequest;
import it.jump3.rest.model.BitlyResponse;
import it.jump3.util.EnvironmentConstants;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BitlyService {

    @Inject
    @RestClient
    BitlyClient bitlyClient;

    @Inject
    ConfigService configService;

    public String getShortUrl(String url) {

        BitlyResponse response = bitlyClient.shorten(EnvironmentConstants.AUTHORIZATION_HEADER_PREFIX + configService.getBitlyToken(),
                new BitlyRequest(url));

        return response.getLink();
    }
}
