package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.OriginalCall;
import com.tsm.cards.repository.OriginalCallRepository;

public class OriginalCallServiceTest {

    @InjectMocks
    private OriginalCallService service;

    @Mock
    private OriginalCallRepository mockRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nullOriginalGiven_ShouldThrowException() {
        // Set up
        OriginalCall originalCall = null;

        // Do test
        try {
            service.save(originalCall);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void validOriginalGiven_ShouldSave() {
        // Set up
        OriginalCall originalCall = buildOriginalCall("home");

        // Expectations
        when(mockRepository.save(originalCall)).thenReturn(originalCall);

        // Do test
        OriginalCall returns = service.save(originalCall);

        // Assertions
        verify(mockRepository).save(originalCall);

        assertThat(returns, is(originalCall));
    }

    @Test
    public void nullWordId_ShouldThrowException() {
        // Set up
        String word = null;

        // Do test
        try {
            service.findOriginalCallById(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void validWordId_ShouldReturnOriginalCallNotFound() {
        // Set up
        String word = "home";

        // Expectations
        Optional<OriginalCall> emptyOptional = Optional.empty();
        when(mockRepository.findById(word)).thenReturn(emptyOptional);

        // Do test
        try {
            service.findOriginalCallById(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockRepository).findById(word);
    }

    @Test
    public void validWordIdGiven_OriginalCallFound() {
        // Set up
        String word = "word";
        // Expectations
        OriginalCall originalCall = buildOriginalCall(word);
        Optional<OriginalCall> expectedOptional = Optional.of(originalCall);
        when(mockRepository.findById(word)).thenReturn(expectedOptional);

        // Do test
        OriginalCall result = service.findOriginalCallById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, is(originalCall));
    }

    private OriginalCall buildOriginalCall(String id) {
        OriginalCall call = new OriginalCall();
        call.setId(id);
        return call;
    }

}
