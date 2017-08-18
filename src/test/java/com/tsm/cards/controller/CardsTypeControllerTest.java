package com.tsm.cards.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.model.CardType;
import com.tsm.cards.parser.CardTypeParser;
import com.tsm.cards.resources.CardTypeResource;
import com.tsm.cards.service.CardTypeService;
import com.tsm.cards.util.CardTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CardsTypeControllerTest {

    @Mock
    private CardTypeService mockService;

    @Mock
    private CardTypeParser mockParser;

    @InjectMocks
    private CardsTypeController controller;

    @Mock
    private Validator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void save_InvalidCardTypeResourceGiven_ShouldThrowException() {
        // Set up
        CardTypeResource resource = CardTypeTestBuilder.buildResource();

        // Expectations
        when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

        // Do test
        try {
            controller.save(resource);
            fail();
        } catch (ValidationException e) {
        }

        // Assertions
        verify(validator).validate(resource, Default.class);
        verifyZeroInteractions(mockService, mockParser);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void save_DuplicatedCardTypeResourceGiven_ShouldSaveCardType() {
        // Set up
        CardTypeResource resource = CardTypeTestBuilder.buildResource();
        CardType cardType = CardTypeTestBuilder.buildModel();

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockParser.toModel(resource)).thenReturn(cardType);
        when(mockService.save(cardType)).thenThrow(BadRequestException.class);

        // Do test
        try {
            controller.save(resource);
            fail();
        } catch (BadRequestException e) {
        }

        // Assertions
        verify(validator).validate(resource, Default.class);
        verify(mockService).save(cardType);
        verify(mockParser).toModel(resource);

    }

    @Test
    public void save_ValidCardTypeResourceGiven_ShouldSaveCardType() {
        // Set up
        CardTypeResource resource = CardTypeTestBuilder.buildResource();
        CardType cardType = CardTypeTestBuilder.buildModel();

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockParser.toModel(resource)).thenReturn(cardType);
        when(mockService.save(cardType)).thenReturn(cardType);
        when(mockParser.toResource(cardType)).thenReturn(resource);

        // Do test
        CardTypeResource result = controller.save(resource);

        // Assertions
        verify(validator).validate(resource, Default.class);
        verify(mockService).save(cardType);
        verify(mockParser).toModel(resource);
        verify(mockParser).toResource(cardType);

        assertNotNull(result);
        assertThat(result, is(resource));
    }
}