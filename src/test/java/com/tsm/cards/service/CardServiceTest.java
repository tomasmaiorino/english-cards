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

import com.tsm.cards.exceptions.BadRequestException;
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

}
