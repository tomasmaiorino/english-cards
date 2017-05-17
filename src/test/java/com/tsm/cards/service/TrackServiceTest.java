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
import com.tsm.cards.model.Track;
import com.tsm.cards.repository.TrackRepository;

public class TrackServiceTest {

	@InjectMocks
	private TrackService service;

	@Mock
	private TrackRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullTrackGiven_ShouldThrowException() throws Exception {
		// Set up
		Track track = null;

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
		Track track = new Track();
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
	public void findTrackById_ShouldReturnOriginalCallNotFound() {
		// Set up
		String word = "home";

		// Expectations
		Optional<Track> emptyOptional = Optional.empty();
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
		Track track = new Track();
		track.setId(word);

		// Expectations
		Optional<Track> expectedOptional = Optional.of(track);
		when(mockRepository.findById(word)).thenReturn(expectedOptional);

		// Do test
		Track result = service.findTrackById(word);

		// Assertions
		verify(mockRepository).findById(word);
		assertThat(result, is(track));
	}

	@Test
	public void incrementTrack_NullTrackGiven_ShouldThrowException() throws Exception {
		// Set up
		Track track = null;

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
		Track track = new Track();
		track.setId(word);

		// Expectations
		Optional<Track> emptyOptional = Optional.empty();
		when(mockRepository.findById(word)).thenReturn(emptyOptional);
		when(mockRepository.save(track)).thenReturn(track);

		// Do test
		Track result = service.incrementTrack(track);

		verify(mockRepository).findById(word);
		assertThat(result, is(track));
	}

	@Test
	public void incrementTrack_EditTrackGiven() throws Exception {
		// Set up
		String word = "home";
		Track track = new Track();
		track.setCallCount(1l);
		track.setId(word);

		// Expectations
		Optional<Track> expectedOptional = Optional.of(track);
		when(mockRepository.findById(word)).thenReturn(expectedOptional);
		when(mockRepository.save(track)).thenReturn(track);

		// Do test
		Track result = service.incrementTrack(track);

		verify(mockRepository).findById(word);
		verify(mockRepository).save(track);
		assertThat(result, is(track));
	}

}
