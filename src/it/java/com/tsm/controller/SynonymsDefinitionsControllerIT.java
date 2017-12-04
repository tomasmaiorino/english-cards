package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

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
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.EnglishCardsApplication;
import com.tsm.builders.KnownWordsBuilder;
import com.tsm.cards.definition.repository.KnownWordRepository;
import com.tsm.cards.definition.repository.SynonymsDefinitionRepository;
import com.tsm.cards.documents.KnownWord;
import com.tsm.cards.documents.SynonymsDefinition;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class SynonymsDefinitionsControllerIT extends BaseTestIT {

	public static String DEFINITIONS_END_POINT = "/api/v1/definitions/X/synonyms";

	private static final String SYNONYMS_OXFORD_SERVICE_SAMPLE_FILE_NAME = "synonyms_response.json";

	private static final Integer MAX_SYNONYMS_RESPONSE = 6;

	public String[] validWords = new String[] { "at", "yours", "all", "home", "word", "house", "car", "normal" };

	public String[] validWordsToSort = new String[] { "yours", "all", "word", "house" };

	private List<KnownWord> knownWords;

	private boolean cachedLoad = false;

	@LocalServerPort
	private int port;

	@Autowired
	private KnownWordRepository knownWordRepository;

	@Autowired
	private SynonymsDefinitionRepository definitionRepository;

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
			SynonymsDefinition cachedDefinition = buidDefinitionFromFile();
			// cachedDefinition.forEach(d -> definitionRepository.save(d));
			definitionRepository.save(cachedDefinition);
			cachedLoad = true;
		}
		RestAssured.port = port;
	}

	@Test
	public void findByWord_UnknownWordGiven_ShouldReturnError() throws URISyntaxException {
		// Set Up
		String word = "ikkf";

		// Do Test
		given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT.replace("X", word)).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is("Unknown word given."));
	}

	@Test
	public void findByWord_NotFoundWordGiven_ShouldReturnError() throws URISyntaxException {
		// Set Up
		String word = "at";

		// Do Test
		given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT.replace("X", word)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body("message", is("Synonyms not found."));
	}

	@Test
	public void findByWord_ValidCachedWordGiven_ShouldReturnDefinition() throws URISyntaxException {
		// Set Up
		String word = "normal";

		// Do Test
		given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT.replace("X", word)).then()
				.statusCode(HttpStatus.OK.value()).body("definitions.size()", is(1))
				.body("definitions[0].word", is(word))
				.body("definitions[0].synonyms.size()", is(MAX_SYNONYMS_RESPONSE));

	}

	@Test
	public void findByWord_ValidNotCachedWordGiven_ShouldReturnDefinition() throws URISyntaxException {
		// Set Up
		String word = validWordsToSort[RandomUtils.nextInt(0, validWordsToSort.length - 1)];

		// Do Test
		given().contentType(ContentType.JSON).when().get(DEFINITIONS_END_POINT.replace("X", word)).then()
				.statusCode(HttpStatus.OK.value()).body("definitions.size()", is(1))
				.body("definitions[0].word", is(word))
				.body("definitions[0].synonyms.size()", is(MAX_SYNONYMS_RESPONSE));

	}

	private List<KnownWord> loadWords() {
		List<String> words = Arrays.asList(validWords);
		return new KnownWordsBuilder().words(words).build();
	}

	private SynonymsDefinition buidDefinitionFromFile() {
		Gson gson = new GsonBuilder().create();

		return gson.fromJson(readingTemplateContent(SYNONYMS_OXFORD_SERVICE_SAMPLE_FILE_NAME),
				SynonymsDefinition.class);
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
