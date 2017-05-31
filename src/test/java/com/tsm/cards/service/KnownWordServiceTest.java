package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
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

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.repository.KnownWordRepository;

@FixMethodOrder(MethodSorters.JVM)
public class KnownWordServiceTest {

    @Mock
    private KnownWordRepository mockRepository;

    @InjectMocks
    private KnownWordService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByWord_NullWordGiven_ShouldThrowException() {
        // Set up
        String word = null;

        // Do test
        try {
            service.findByWord(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);

    }

    @Test
    public void findByWord_emptyWordGiven_ShouldThrowException() {
        // Set up
        String word = "";

        // Do test
        try {
            service.findByWord(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void findByWord_validWordGiven_NotWordFound_ShouldThrowException() {
        // Set up
        String word = "word";

        // Expectations
        Optional<KnownWord> emptyOptional = Optional.empty();
        when(mockRepository.findByWord(word)).thenReturn(emptyOptional);

        // Optional<Contact> expectedOptional = Optional.of(contact);

        // Do test
        try {
            service.findByWord(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockRepository).findByWord(word);
    }

    @Test
    public void findByWord_validWordGiven_KnownWordFound() {
        // Set up
        String word = "word";

        // Expectations
        KnownWord knownWord = buildKnownWord();
        Optional<KnownWord> expectedOptional = Optional.of(knownWord);
        when(mockRepository.findByWord(word)).thenReturn(expectedOptional);

        // Do test
        KnownWord result = service.findByWord(word);

        // Assertions
        verify(mockRepository).findByWord(word);

        assertThat(result, is(knownWord));
    }

    @Test
    public void save_NullKnownWordGiven_ShouldThrowException() {
        // Set up
        KnownWord knownWord = null;

        // Do test
        try {
            service.save(knownWord);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void save_ValidKnownWordGiven_ShouldSave() {
        // Set up
        KnownWord knownWord = buildKnownWord();

        // Expectations
        when(mockRepository.save(knownWord)).thenReturn(knownWord);

        // Do test
        KnownWord result = service.save(knownWord);

        // Assertions
        verify(mockRepository).save(knownWord);
        assertThat(result, is(knownWord));
    }

    private KnownWord buildKnownWord() {
        KnownWord knownWord = new KnownWord();
        ReflectionTestUtils.setField(knownWord, "id", "0");
        return knownWord;
    }

}
