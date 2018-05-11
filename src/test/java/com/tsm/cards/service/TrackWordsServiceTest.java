package com.tsm.cards.service;

import com.tsm.cards.definition.repository.TrackWordRepository;
import com.tsm.cards.documents.TrackWord;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class TrackWordsServiceTest {

    @InjectMocks
    private TrackWordsService service;

    @Mock
    private TrackWordRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_NullTrackGiven_ShouldThrowException() throws Exception {
        // Set up
        TrackWord track = null;

        // Do test
        try {
            service.save(track);
            fail();
        } catch (IllegalArgumentException e) {
        }

        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void save_ValidTrackGiven() throws Exception {
        // Set up
        TrackWord track = new TrackWord();
        track.setId("home");

        // Expectations
        when(mockRepository.save(track)).thenReturn(track);

        // Do test
        service.save(track);

        verify(mockRepository).save(track);
    }

    @Test
    public void findTrackById_NullWordGiven_ShouldThrowException() throws Exception {
        // Set up
        String word = null;
        // Do test
        try {
            service.findTrackById(word);
            fail();
        } catch (IllegalArgumentException e) {
        }
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void findTrackById_ShouldReturnDefinitionNotFound() {
        // Set up
        String word = "home";

        // Expectations
        Optional<TrackWord> emptyOptional = Optional.empty();
        when(mockRepository.findById(word)).thenReturn(emptyOptional);

        // Do test
        try {
            service.findTrackById(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockRepository).findById(word);
    }

    @Test
    public void findTrackById_ValidWorkGiven_TrackFound() {
        // Set up
        String word = "word";
        TrackWord track = new TrackWord();
        track.setId(word);

        // Expectations
        Optional<TrackWord> expectedOptional = Optional.of(track);
        when(mockRepository.findById(word)).thenReturn(expectedOptional);

        // Do test
        TrackWord result = service.findTrackById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, is(track));
    }

    @Test
    public void incrementTrack_NullTrackGiven_ShouldThrowException() throws Exception {
        // Set up
        TrackWord track = null;

        // Do test
        try {
            service.incrementTrack(track);
            fail();
        } catch (IllegalArgumentException e) {
        }

        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void incrementTrack_NewTrackGiven() throws Exception {
        // Set up
        String word = "home";
        TrackWord track = new TrackWord();
        track.setId(word);

        // Expectations
        Optional<TrackWord> emptyOptional = Optional.empty();
        when(mockRepository.findById(word)).thenReturn(emptyOptional);
        when(mockRepository.save(track)).thenReturn(track);

        // Do test
        TrackWord result = service.incrementTrack(track);

        verify(mockRepository).findById(word);

        // Assertions
        assertThat(result, allOf(
            hasProperty("id", is(word)),
            hasProperty("callCount", is(1l))));
    }

    @Test
    public void incrementTrack_EditTrackGiven() throws Exception {
        // Set up
        String word = "home";
        TrackWord track = new TrackWord();
        track.setCallCount(1l);
        track.setId(word);

        // Expectations
        Optional<TrackWord> expectedOptional = Optional.of(track);
        when(mockRepository.findById(word)).thenReturn(expectedOptional);
        when(mockRepository.save(track)).thenReturn(track);

        // Do test
        TrackWord result = service.incrementTrack(track);

        verify(mockRepository).findById(word);
        verify(mockRepository).save(track);
        assertThat(result, is(track));
    }

    @Test
    public void incrementTrack_NullWordGiven_ShouldThrowException() throws Exception {
        // Set up
        String word = null;

        // Do test
        try {
            service.incrementTrack(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void incrementTrack_WordGiven_ShouldReturnNewTrack() throws Exception {
        // Set up
        String word = "home";

        TrackWord track = new TrackWord();
        track.setId(word);

        // Expectations
        Optional<TrackWord> emptyOptional = Optional.empty();
        when(mockRepository.findById(word)).thenReturn(emptyOptional);
        when(mockRepository.save(track)).thenReturn(track);

        // Do test
        TrackWord result = service.incrementTrack(word);

        verify(mockRepository).findById(word);

        // Assertions
        assertThat(result, allOf(
            hasProperty("id", is(word)),
            hasProperty("callCount", is(1l))));
    }

    @Test
    public void incrementTrack_ValidWordGiven() throws Exception {
        // Set up
        String word = "home";

        TrackWord track = new TrackWord();
        track.setCallCount(1l);
        track.setId(word);

        // Expectations
        Optional<TrackWord> expectedOptional = Optional.of(track);
        when(mockRepository.findById(word)).thenReturn(expectedOptional);
        when(mockRepository.save(track)).thenReturn(track);

        // Do test
        TrackWord result = service.incrementTrack(word);

        verify(mockRepository).findById(word);
        verify(mockRepository).save(track);
        // Assertions
        assertThat(result, allOf(
            hasProperty("id", is(word)),
            hasProperty("callCount", is(2l))));
    }

}
