package com.tsm.cards.service;

import com.tsm.cards.resources.ContactResource;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * Created by tomas on 6/10/18.
 */
@Slf4j
public class ContactService {

    public boolean sendContactMessage(final ContactResource contact) {
        Assert.notNull(contact, "The contact must not be null!");
        log.info("Sending contact message [{}].");
        RestTemplate restTemplate = new RestTemplate();
        return  false;
    }
}
