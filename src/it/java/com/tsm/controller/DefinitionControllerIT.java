package com.tsm.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tsm.EnglishCardsApplication;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.repository.KnownWordRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class DefinitionControllerIT {

    private static final String EMPTY_WORDS_ERROR_MESSAGE = "{\"message\":\"The words must not be empty.\"}";

    private static final String NONE_VALID_WORDS_GIVEN = "\"None valid words was given: %s\"";

    private List<KnownWord> knownWords;

    @LocalServerPort
    private int port;

    @Autowired
    private KnownWordRepository knownWordRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        // load mongo known words
        if (knownWords == null) {
            knownWords = loadWords();
            knownWords.forEach(k -> {
                knownWordRepository.save(k);
            });
        }
    }

    @Test
    public void getDefinitions_NullWordsGiven_ShouldReturnError() {
        // Set Up
        String words = null;
        HttpEntity<String> entity = new HttpEntity<String>(words, headers);

        // Do Test
        ResponseEntity<String> response = restTemplate.exchange(
            createURLWithPort("/definition"),
            HttpMethod.POST, entity, String.class);

        // Assertions
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assert.assertEquals(EMPTY_WORDS_ERROR_MESSAGE, response.getBody());
    }

    @Test
    public void getDefinitions_InvalidWordsGiven_ShouldReturnError() throws URISyntaxException {
        // Set Up
        String words = "terrrer";
        // HttpEntity<String> entity = new HttpEntity<String>(words, headers);
        //        RequestEntity<String> request = RequestEntity.post(new URI(createURLWithPort("/definition"))).body(words);
        // Do Test
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/definition"), words, String.class);
        // HttpMethod.POST, entity, String.class);

        // Assertions
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assert.assertEquals("{\"message\":" + String.format(NONE_VALID_WORDS_GIVEN, words), response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private List<KnownWord> loadWords() {
        List<KnownWord> words = new ArrayList<>();
        KnownWord knownWord = new KnownWord();
        knownWord.setWord("at");
        knownWord = new KnownWord();
        words.add(knownWord);
        knownWord = new KnownWord();
        knownWord.setWord("your");
        words.add(knownWord);
        knownWord = new KnownWord();
        knownWord.setWord("all");
        words.add(knownWord);
        knownWord = new KnownWord();
        knownWord.setWord("home");
        words.add(knownWord);
        knownWord = new KnownWord();
        knownWord.setWord("word");
        words.add(knownWord);
        knownWord = new KnownWord();
        knownWord.setWord("house");
        words.add(knownWord);
        knownWord = new KnownWord();
        knownWord.setWord("car");
        return words;
    }
}
