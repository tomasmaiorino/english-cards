package com.tsm.cards.definition.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tsm.cards.documents.KnownWord;
import com.tsm.cards.exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile(value = "local")
@Slf4j
public class InitialDefinitionsLoad implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    @Getter
    @Setter
    private KnownWordService knownWordService;

    @Value("${initial.load.word}")
    @Getter
    @Setter
    private String initialLoadWord;

    private static final String KNOWN_WORDS_FILE_NAME = "known_words.json";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Initial load ->");
        try {
            knownWordService.findByWord(getInitialLoadWord());
            log.info("Initial load word found :) ");
        } catch (BadRequestException e) {
            log.info("initial load word [{}] not found. Let's run the initial load :O ", getInitialLoadWord());
            runInitialLoad();
        } catch (Exception e) {
            log.error("Something went wrong.", e);
        }
    }

    private void runInitialLoad() {
        log.info("initial load ->");
        List<KnownWord> words = buidDefinitionFromFile();
        if (words != null && !words.isEmpty()) {
            words.forEach(w -> {
                log.info("saving [{}]", w.getWord());
                knownWordService.save(w);
            });
        }
        log.info("initial load <-");
    }

    private List<KnownWord> buidDefinitionFromFile() {
        Gson gson = new GsonBuilder().create();

        Type listType = new TypeToken<ArrayList<KnownWord>>() {
        }.getType();

        List<KnownWord> knownWord = gson.fromJson(readingTemplateContent(KNOWN_WORDS_FILE_NAME), listType);
        return knownWord;
    }

    private String readingTemplateContent(String fileName) {
        log.info("Loading from file [{}]", fileName);
        try {
            File file = new ClassPathResource(fileName).getFile();
            return new String(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
        }
        return "";
    }
}