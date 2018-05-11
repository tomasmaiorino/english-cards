package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ForbiddenRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.tsm.cards.util.ErrorCodes.ACCESS_NOT_ALLOWED;
import static com.tsm.cards.util.ErrorCodes.MISSING_HEADER;

@Service
@Slf4j
public class AssertClientRequest {

    @Value(value = "${client.service.key}")
    private String clientServiceKey;

    @Value(value = "${client.service.url}")
    private String clientServiceUrl;

    public void assertClientHeader(final String adminHeader) {

        if (StringUtils.isBlank(adminHeader)) {
            throw new BadRequestException(MISSING_HEADER);
        }

        if (!isRequestAllowedCheckingAdminToken(adminHeader)) {
            throw new ForbiddenRequestException(ACCESS_NOT_ALLOWED);
        }

    }

    public boolean isRequestAllowedCheckingRequestUrl(final String requestUrl, final String adminToken) {
        Assert.hasText(requestUrl, "The requestUrl must not be null or empty!");
        Assert.hasText(adminToken, "The adminToken must not be null or empty!");
        log.info("Checking request url for unregistered requests [{}].", requestUrl);
        log.info("Checking adminToken for unregistered requests [{}].", adminToken);

        boolean allowedClient = clientServiceKey.equals(adminToken) && requestUrl.contains(clientServiceUrl);

        log.debug("Allowed request [{}].", allowedClient);

        return allowedClient;
    }

    private boolean isRequestAllowedCheckingAdminToken(final String adminToken) {
        Assert.hasText(adminToken, "The adminToken must not be null or empty!");
        log.info("Checking request token for unregistered requests [{}].", adminToken);

        boolean allowedClient = clientServiceKey.equals(adminToken);

        log.debug("Allowed request [{}].", allowedClient);

        return allowedClient;
    }

}
