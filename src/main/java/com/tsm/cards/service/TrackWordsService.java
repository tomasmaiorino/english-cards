package com.tsm.cards.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.TrackWord;
import com.tsm.cards.repository.TrackWordRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class TrackWordsService {

    @Autowired
    private TrackWordRepository repository;

    @Transactional
    public TrackWord save(TrackWord track) {
        Assert.notNull(track, "The Track must not be null.");
        log.info("Saving track [{}] .", track);

        repository.save(track);

        log.info("Saved definition [{}].", track);
        return track;
    }

    public TrackWord findTrackById(final String word) {
        Assert.notNull(word, "The id must not be null.");

        log.info("Searching for track cache [{}] .", word);

        TrackWord track = findOptionalTrackById(word).orElseThrow(() -> new ResourceNotFoundException("not found"));

        log.info("Found track [{}] .", track);

        return track;

    }

    private Optional<TrackWord> findOptionalTrackById(final String word) {
        Assert.notNull(word, "The id must not be null.");

        log.info("Searching for track cache [{}] .", word);

        return repository.findById(word);

    }

    public TrackWord incrementTrack(final String word) {
        Assert.notNull(word, "The word must not be null.");

        TrackWord trackTemp = null;
        Optional<TrackWord> optional = findOptionalTrackById(word);

        if (optional.isPresent()) {
            trackTemp = optional.get();
            trackTemp.setCallCount(trackTemp.getCallCount() + 1);
        } else {
            trackTemp = new TrackWord();
            trackTemp.setId(word);
        }

        save(trackTemp);

        log.info("Incremented track word [{}].", trackTemp);
        return trackTemp;
    }

    public TrackWord incrementTrack(final TrackWord track) {
        Assert.notNull(track, "The Track must not be null.");
        log.info("Incrementing track [{}] .", track);
        return incrementTrack(track.getId());
    }

}
