package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.EnglishCardsApplication;
import com.tsm.builders.DefinitionResourceBuilder;
import com.tsm.builders.KnownWordsBuilder;
import com.tsm.cards.model.Definition;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.repository.DefinitionRepository;
import com.tsm.cards.repository.KnownWordRepository;
import com.tsm.cards.resources.DefinitionResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class DefinitionControllerIT {

    private static final String EMPTY_WORDS_ERROR_MESSAGE = "The words must not be empty.";

    private static final String NONE_VALID_WORDS_GIVEN = "None valid words was given: %s";

    private static final String OXFORD_SERVICE_SAMPLE_FILE_NAME = "oxford.json";
    
    public String[] validWords = new String[] { "at", "your", "all", "home", "word", "house", "car" };
    
    public String[] validWordsToSort = new String[] { "at", "your", "all", "word", "house"};

    private List<KnownWord> knownWords;

    private boolean cachedLoad = false;

    @LocalServerPort
    private int port;

    @Autowired
    private KnownWordRepository knownWordRepository;

    @Autowired
    private DefinitionRepository definitionRepository;

    @Before
    public void setUp() {
        // load mongo known words
        if (knownWords == null) {
            knownWords = loadWords();
            knownWords.forEach(k -> {
                knownWordRepository.save(k);
            });
        }
        if (!cachedLoad) {
            List<Definition> cachedDefinition = buidDefinitionFromFile();
            cachedDefinition.forEach(d -> definitionRepository.save(d));
            cachedLoad = true;
        }
        RestAssured.port = port;
    }

    @Test
    // @Ignore
    public void getDefinitions_NullWordsGiven_ShouldReturnError() {
        // Set Up
        Set<String> words = new HashSet<>();
        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();

        // Do Test
        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is(EMPTY_WORDS_ERROR_MESSAGE));
    }

    @Test
    // @Ignore
    public void getDefinitions_InvalidWordsGiven_ShouldReturnError() throws URISyntaxException {
        // Set Up
        String word = "ikkf";

        Set<String> words = new HashSet<>();
        words.add(word);
        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();

        // Do Test
        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("message", is(String.format(NONE_VALID_WORDS_GIVEN, words)));
    }

    @Test
    // @Ignore
    public void getDefinitions_CachedWordAndInvalidWordsGiven_ShouldReturnDefinition() throws URISyntaxException {
        // Set Up
        String word = "home";
        String car = "Car";
        String invalidWord = "klkkl";
        Set<String> words = new HashSet<>();
        words.add(word);
        words.add(car);
        words.add(invalidWord);
        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();

        // Do Test
        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
            .statusCode(HttpStatus.CREATED.value()).body("size()", is(2))
            .body("[0].word", is(car.toLowerCase()))
            .body("[0].definitions.size()", is(greaterThan(0)))
            .body("[1].word", is(word))
            .body("[1].definitions.size()", is(greaterThan(0)));
    }

    @Test
    public void getDefinitions_NotCachedWordGiven_ShouldReturnDefinition() throws URISyntaxException {
        // Set Up
        String word = validWordsToSort[RandomUtils.nextInt(0, validWordsToSort.length - 1)];
        Set<String> words = new HashSet<>();
        words.add(word);
        DefinitionResource resource = new DefinitionResourceBuilder().words(words).build();

        // Do Test
        given().body(resource).contentType(ContentType.JSON).when().post("/definitions").then()
            .statusCode(HttpStatus.CREATED.value()).body("size()", is(1))
            .body("[0].word", is(word.toLowerCase()))
            .body("[0].definitions.size()", is(greaterThan(0)));
    }

    private List<KnownWord> loadWords() {
        List<String> words = Arrays.asList(validWords);
        return new KnownWordsBuilder().words(words).build();
    }

    private List<Definition> buidDefinitionFromFile() {
        Gson gson = new GsonBuilder().create();

        Type listType = new TypeToken<ArrayList<Definition>>() {
        }.getType();

        List<Definition> definitions = gson.fromJson(readingTemplateContent(OXFORD_SERVICE_SAMPLE_FILE_NAME), listType);
        return definitions;
    }

    private String readingTemplateContent(String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            return new String(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
