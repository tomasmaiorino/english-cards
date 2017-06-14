package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.SetWordsCards;
import com.tsm.cards.repository.SetWordsCardsRepository;

@FixMethodOrder(MethodSorters.JVM)
public class SetWordsCardsServiceTest {

    @Mock
    private SetWordsCardsRepository mockRepository;

    @InjectMocks
    private SetWordsCardsService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_NullSetWordsCardsGiven_ShouldThrowException() {
        // Set up
        SetWordsCards cards = null;

        // Do test
        try {
            service.save(cards);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void save_ValidSetWordsCardsGiven_CardSaved() {
        // Set up
        SetWordsCards cards = new SetWordsCards();

        // Expectations
        when(mockRepository.save(cards)).thenReturn(cards);

        // Do test
        SetWordsCards result = service.save(cards);

        // Assertions
        verify(mockRepository).save(cards);
        assertThat(result, is(cards));
    }

    @Test
    public void findAll_SetWordsCardsFound_ShouldReturnContent() {
        // Set up
        SetWordsCards cards = new SetWordsCards();

        // Expectations
        when(mockRepository.findAll()).thenReturn(Collections.singletonList(cards));

        // Do test
        List<SetWordsCards> result = service.findAll();

        // Assertions
        verify(mockRepository).findAll();
        assertNotNull(result);
        assertThat(result.contains(cards), is(true));
    }

    @Test
    public void findAll_SetWordsCardsNotFound_ShouldReturnEmptyContent() {

        // Expectations
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        // Do test
        List<SetWordsCards> result = service.findAll();

        // Assertions
        verify(mockRepository).findAll();
        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }

}
