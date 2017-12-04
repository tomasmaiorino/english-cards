package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Card;
import com.tsm.cards.repository.CardRepository;
import com.tsm.cards.util.CardTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CardServiceTest {

    @InjectMocks
    private CardService service;

    @Mock
    private CardRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_NullCardGiven_ShouldThrowException() {
        // Set up
        Card card = null;

        // Do test
        try {
            service.save(card);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void save_DuplicatedImgUrlGiven_ShouldThrowException() {
        // Set up
        Card card = CardTestBuilder.buildModel();

        // Expectations
        when(mockRepository.findByImgUrl(card.getImgUrl())).thenReturn(Optional.of(card));

        // Do test
        try {
            service.save(card);
            fail();
        } catch (BadRequestException e) {
        }

        // Assertions
        verify(mockRepository).findByImgUrl(card.getImgUrl());
        verify(mockRepository, times(0)).save(card);
    }

    @Test
    public void save_ValidCardGiven_ShouldCreateClient() {
        // Set up
        Card card = CardTestBuilder.buildModel();

        // Expectations
        when(mockRepository.findByImgUrl(card.getImgUrl())).thenReturn(Optional.empty());
        when(mockRepository.save(card)).thenReturn(card);

        // Do test
        Card result = service.save(card);

        // Assertions
        verify(mockRepository).findByImgUrl(card.getImgUrl());

        assertNotNull(result);
        assertThat(result, is(card));
    }

    @Test
    public void update_NullOriginGiven_ShouldThrowException() {
        // Set up
        Card origin = null;
        Card model = CardTestBuilder.buildModel();

        // Do test
        try {
            service.update(origin, model);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void update_NewOriginGiven_ShouldThrowException() {
        // Set up
        Card origin = CardTestBuilder.buildModel();
        Card model = CardTestBuilder.buildModel();

        // Do test
        try {
            service.update(origin, model);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void update_NullModelGiven_ShouldThrowException() {
        // Set up
        Card origin = CardTestBuilder.buildModel();
        Card model = null;

        // Do test
        try {
            service.update(origin, model);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void update_DuplicatedNameGiven_ShouldThrowException() {
        // Set up
        Card origin = CardTestBuilder.buildModel();
        ReflectionTestUtils.setField(origin, "id", 2);
        Card model = CardTestBuilder.buildModel();
        Card duplicated = CardTestBuilder.buildModel();
        ReflectionTestUtils.setField(duplicated, "id", 1);

        // Expectations
        when(mockRepository.findByImgUrl(model.getImgUrl())).thenReturn(Optional.of(duplicated));

        // Do test
        try {
            service.update(origin, model);
            fail();
        } catch (BadRequestException e) {
        }

        // Assertions
        verify(mockRepository).findByImgUrl(model.getImgUrl());
        verify(mockRepository, times(0)).save(origin);
    }

    @Test
    public void update_ValidObjectsGiven_ShouldUpdate() {
        // Set up
        Card origin = CardTestBuilder.buildModel();
        ReflectionTestUtils.setField(origin, "id", 2);
        Card model = CardTestBuilder.buildModel();
        model.setName("new Name");

        // Expectations
        when(mockRepository.findByImgUrl(model.getImgUrl())).thenReturn(Optional.of(origin));
        when(mockRepository.save(origin)).thenReturn(origin);

        // Do test
        Card result = service.update(origin, model);

        // Assertions
        verify(mockRepository).findByImgUrl(model.getImgUrl());

        assertNotNull(result);
        assertThat(result, is(origin));
    }

    @Test
    public void findById_NullIdGiven_ShouldThrowException() {
        // Set up
        Integer cardId = null;

        // Do test
        try {
            service.findById(cardId);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void findById_NotFoundCardGiven_ShouldThrowException() {
        // Set up
        Integer cardId = 1;

        // Expectations
        when(mockRepository.findById(cardId)).thenReturn(Optional.empty());

        // Do test
        try {
            service.findById(cardId);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockRepository).findById(cardId);
    }

    @Test
    public void findById_FoundCardGiven_ShouldReturnContent() {
        // Set up
        Integer cardId = 1;
        Card card = CardTestBuilder.buildModel();

        // Expectations
        when(mockRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Do test
        Card result = service.findById(cardId);

        // Assertions
        verify(mockRepository).findById(cardId);

        assertNotNull(result);
        assertThat(result, is(card));
    }

}
