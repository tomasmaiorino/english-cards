package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.tsm.EnglishCardsApplication;
import com.tsm.builders.DefinitionResourceBuilder;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.repository.KnownWordRepository;
import com.tsm.cards.resources.DefinitionsResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class DefinitionControllerIT {

	private static final String EMPTY_WORDS_ERROR_MESSAGE = "The words must not be empty.";

	private static final String NONE_VALID_WORDS_GIVEN = "None valid words was given: %s";

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
		RestAssured.port = port;
	}

	@Test
	public void getDefinitions_NullWordsGiven_ShouldReturnError() {
		// Set Up
		String words = "";
		given()
			.body(words)
			.when()
			.post("/definitions")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("message",is(EMPTY_WORDS_ERROR_MESSAGE));
	}

	@Test
	public void getDefinitions_InvalidWordsGiven_ShouldReturnError() throws URISyntaxException {
		// Set Up
		String word = "ikkf";
		
		Set<String> words = new HashSet<>();
		words.add(word);
		DefinitionsResource resource = new DefinitionResourceBuilder().words(words).build();
		
		given()
			.body(resource)
			.when()
			.post("/definitions")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("message",is(String.format(NONE_VALID_WORDS_GIVEN)));
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
