package com.tsm.cards.controller;

import com.tsm.cards.resources.ContactResource;
import com.tsm.cards.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by tomas on 6/10/18.
 */
@Slf4j
public class ContactsController extends  BaseController {

    @Autowired
    private ContactService contactService;

    @RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public ContactResource sendMessage(@RequestBody final ContactResource resource, final HttpServletRequest request) {
        log.debug("Received a request to send a message [{}].", resource);

        validate(resource, Default.class);

        contactService.sendContactMessage(resource);

        log.debug("returning resource [{}].", resource);

        return null;
    }

}
