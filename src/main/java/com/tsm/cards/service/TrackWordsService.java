package com.tsm.cards.service;

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

        log.info("Saved originalCall [{}].", track);
        return track;
    }

    public TrackWord findTrackById(String word) {
        Assert.notNull(word, "The id must not be null.");

        log.info("Searching for track cache [{}] .", word);

        TrackWord track = repository.findById(word).orElseThrow(() -> new ResourceNotFoundException("not found"));

        log.info("Found track [{}] .", track);

        return track;

    }

    public TrackWord incrementTrack(TrackWord track) {
        Assert.notNull(track, "The Track must not be null.");
        log.info("incrementing track [{}] .", track);

        TrackWord trackTemp = null;

        try {
            trackTemp = findTrackById(track.getId());
            trackTemp.setCallCount(trackTemp.getCallCount() + 1);

        } catch (ResourceNotFoundException e) {
            track.setCallCount(1l);
            trackTemp = track;

        }

        save(trackTemp);

        log.info("Incremented originalCall [{}].", trackTemp);
        return trackTemp;
    }

}
