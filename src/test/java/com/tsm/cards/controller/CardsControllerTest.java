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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Card;
import com.tsm.cards.model.CardType;
import com.tsm.cards.parser.CardParser;
import com.tsm.cards.resources.CardResource;
import com.tsm.cards.service.CardService;
import com.tsm.cards.service.CardTypeService;
import com.tsm.cards.util.CardTestBuilder;
import com.tsm.cards.util.CardTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CardsControllerTest {

    private static final Integer CARD_ID = 1;

    @Mock
    private CardService mockService;

    @Mock
    private CardTypeService mockCardTypeService;

    @Mock
    private CardParser mockParser;

    @InjectMocks
    private CardsController controller;

    @Mock
    private Validator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void save_InvalidCardResourceGiven_ShouldThrowException() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();

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
    public void save_NotFoundCardTypeResourceGiven_ShouldSaveCard() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockCardTypeService.findById(resource.getCardType())).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.save(resource);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verifyZeroInteractions(mockParser, mockService);
        verify(validator).validate(resource, Default.class);
        verify(mockCardTypeService).findById(resource.getCardType());

    }

    @Test
    public void save_ValidCardResourceGiven_ShouldSaveCard() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();
        CardType cardType = CardTypeTestBuilder.buildModel();
        Card card = CardTestBuilder.buildModel();
        card.setCardType(cardType);

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockCardTypeService.findById(resource.getCardType())).thenReturn(cardType);
        when(mockParser.toModel(resource, cardType)).thenReturn(card);
        when(mockService.save(card)).thenReturn(card);
        when(mockParser.toResource(card)).thenReturn(resource);

        // Do test
        CardResource result = controller.save(resource);

        // Assertions
        verify(validator).validate(resource, Default.class);
        verify(mockService).save(card);
        verify(mockParser).toModel(resource, cardType);
        verify(mockCardTypeService).findById(resource.getCardType());
        verify(mockParser).toResource(card);

        assertNotNull(result);
        assertThat(result, is(resource));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void update_NotFoundCardResourceGiven_ShouldSaveCard() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockService.findById(CARD_ID)).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.update(CARD_ID, resource);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verifyZeroInteractions(mockParser, mockCardTypeService);
        verify(validator).validate(resource, Default.class);
        verify(mockService).findById(CARD_ID);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void update_NotFoundCardTypeResourceGiven_ShouldSaveCard() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();
        Card card = CardTestBuilder.buildModel();
        ReflectionTestUtils.setField(card, "id", CARD_ID);

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockService.findById(CARD_ID)).thenReturn(card);
        when(mockCardTypeService.findById(resource.getCardType())).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.update(CARD_ID, resource);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verifyZeroInteractions(mockParser);
        verify(validator).validate(resource, Default.class);
        verify(mockService).findById(CARD_ID);
        verify(mockCardTypeService).findById(resource.getCardType());
    }

    @Test
    public void update_ValidCardResourceGiven_ShouldSaveCard() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();
        CardType cardType = CardTypeTestBuilder.buildModel();
        Card model = CardTestBuilder.buildModel();
        model.setCardType(cardType);

        Card origin = CardTestBuilder.buildModel();
        ReflectionTestUtils.setField(origin, "id", CARD_ID);

        // Expectations
        when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
        when(mockCardTypeService.findById(resource.getCardType())).thenReturn(cardType);
        when(mockParser.toModel(resource, cardType)).thenReturn(model);
        when(mockService.update(origin, model)).thenReturn(origin);
        when(mockParser.toResource(origin)).thenReturn(resource);
        when(mockService.findById(CARD_ID)).thenReturn(origin);

        // Do test
        CardResource result = controller.update(CARD_ID, resource);

        // Assertions
        verify(validator).validate(resource, Default.class);
        verify(mockService).update(origin, model);
        verify(mockParser).toModel(resource, cardType);
        verify(mockCardTypeService).findById(resource.getCardType());
        verify(mockParser).toResource(origin);

        assertNotNull(result);
        assertThat(result, is(resource));
    }

    @Test
    public void findById_NotFoundCardGiven_ShouldThrowException() {

        // Expectations
        when(mockService.findById(CARD_ID)).thenThrow(new ResourceNotFoundException(""));

        // Do test
        try {
            controller.findById(CARD_ID);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockService).findById(CARD_ID);
        verifyZeroInteractions(mockParser);
    }

    @Test
    public void findById_FoundCardGiven_ShouldReturnContent() {
        // Set up
        CardResource resource = CardTestBuilder.buildResource();
        Card card = CardTestBuilder.buildModel();

        // Expectations
        when(mockService.findById(CARD_ID)).thenReturn(card);
        when(mockParser.toResource(card)).thenReturn(resource);

        // Do test
        CardResource result = controller.findById(CARD_ID);

        // Assertions
        verify(mockService).findById(CARD_ID);
        verify(mockParser).toResource(card);

        assertNotNull(result);
        assertThat(result, is(resource));
    }
}
