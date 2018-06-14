package com.tsm.cards.service;

import com.tsm.cards.resources.ContactResource;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by tomas on 6/10/18.
 */
@Slf4j
@Service
public class ContactService {

    @Value("${contact.application.url}")
    private String contactApplicationUrl;

    @Value("${englishCardClientToken}")
    private String englishCardClientToken;

    @Autowired
    private RestTemplate restTemplate;

    public boolean sendContactMessage(final ContactResource contact) {
        Assert.notNull(contact, "The contact must not be null!");
        log.info("Sending contact message [{}].");
        ResponseEntity<ContactResource> exchange =
                this.restTemplate.exchange(
                        contactApplicationUrl,
                        HttpMethod.POST,
                        null,
                        new ParameterizedTypeReference<ContactResource>() {
                        },
                        englishCardClientToken);
        return  false;
    }
}
