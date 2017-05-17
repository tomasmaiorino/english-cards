package com.tsm.cards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Track;
import com.tsm.cards.repository.TrackRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class TrackService {

	@Autowired
	private TrackRepository repository;

	@Transactional
	public Track save(Track track) {
		Assert.notNull(track, "The Track must not be null.");
		log.info("Saving track [{}] .", track);

		repository.save(track);

		log.info("Saved originalCall [{}].", track);
		return track;
	}

	public Track findTrackById(String word) {
		Assert.notNull(word, "The id must not be null.");

		log.info("Searching for track cache [{}] .", word);

		Track track = repository.findById(word).orElseThrow(() -> new ResourceNotFoundException("not found"));

		log.info("Found track [{}] .", track);

		return track;

	}

	public Track incrementTrack(Track track) {
		Assert.notNull(track, "The Track must not be null.");
		log.info("Saving track [{}] .", track);

		Track trackTemp = null;

		try {
			trackTemp = findTrackById(track.getId());
			trackTemp.setCallCount(trackTemp.getCallCount() + 1);
		} catch (ResourceNotFoundException e) {
			trackTemp.setCallCount(1l);
		}

		save(trackTemp);

		log.info("Saved originalCall [{}].", trackTemp);
		return trackTemp;
	}

}
