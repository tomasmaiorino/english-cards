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
import com.tsm.cards.model.CardType;
import com.tsm.cards.repository.CardTypeRepository;
import com.tsm.cards.util.CardTypeTestBuilder;
@FixMethodOrder(MethodSorters.JVM)
public class CardTypeServiceTest {

    @InjectMocks
    private CardTypeService service;

    @Mock
    private CardTypeRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_NullCardTypeGiven_ShouldThrowException() {
        // Set up
        CardType cardType = null;

        // Do test
        try {
            service.save(cardType);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void save_DuplicatedNameGiven_ShouldThrowException() {
        // Set up
        CardType cardType = CardTypeTestBuilder.buildModel();

        // Expectations
        when(mockRepository.findByName(cardType.getName())).thenReturn(Optional.of(cardType));

        // Do test
        try {
            service.save(cardType);
            fail();
        } catch (BadRequestException e) {
        }

        // Assertions
        verify(mockRepository).findByName(cardType.getName());
        verify(mockRepository, times(0)).save(cardType);
    }

    @Test
    public void save_ValidCardTypeGiven_ShouldCreateClient() {
        // Set up
        CardType cardType = CardTypeTestBuilder.buildModel();

        // Expectations
        when(mockRepository.findByName(cardType.getName())).thenReturn(Optional.empty());
        when(mockRepository.save(cardType)).thenReturn(cardType);

        // Do test
        CardType result = service.save(cardType);

        // Assertions
        verify(mockRepository).findByName(cardType.getName());

        assertNotNull(result);
        assertThat(result, is(cardType));
    }

}
