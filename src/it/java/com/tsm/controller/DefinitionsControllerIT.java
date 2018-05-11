package com.tsm.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.EnglishCardsApplication;
import com.tsm.builders.KnownWordsBuilder;
import com.tsm.cards.definition.repository.DefinitionRepository;
import com.tsm.cards.definition.repository.KnownWordRepository;
import com.tsm.cards.documents.Definition;
import com.tsm.cards.documents.KnownWord;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class DefinitionsControllerIT extends BaseTestIT {

    public static final String DEFINITIONS_END_POINT = "/api/v1/definitions/";

    private static final String OXFORD_SERVICE_SAMPLE_FILE_NAME = "oxford.json";

    public String[] validWords = new String[] { "at", "your", "all", "home", "word", "house", "car" };

    public String[] validWordsToSort = new String[] { "at", "your", "all", "word", "house" };

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
    public void findByWord_UnknownWordGiven_ShouldReturnError() throws URISyntaxException {
        // Set Up
        String word = "ikkf";

        // Do Test
        given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT + word).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("message", is("Unknown word given."));
    }

    @Test
    public void findByWord_ValidCachedWordGiven_ShouldReturnDefinition() throws URISyntaxException {
        // Set Up
        String word = "home";

        // Do Test
        given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT + word).then()
            .statusCode(HttpStatus.OK.value()).body("definitions.size()", is(1))
            .body("definitions[0].word", is(word))
            .body("definitions[0].definitions.size()", is(4));

    }

    @Test
    public void findByWord_ValidNotCachedWordGiven_ShouldReturnDefinition() throws URISyntaxException {
        // Set Up
        String word = validWordsToSort[RandomUtils.nextInt(0, validWordsToSort.length - 1)];

        // Do Test
        given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT + word).then()
            .statusCode(HttpStatus.OK.value()).body("definitions.size()", is(1))
            .body("definitions[0].word", is(word))
            .body("definitions[0].definitions.size()", is(4));

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
