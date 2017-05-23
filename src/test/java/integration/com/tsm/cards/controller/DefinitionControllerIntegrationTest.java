package integration.com.tsm.cards.controller;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tsm.EnglishCardsApplication;
import com.tsm.cards.model.KnownWord;
import com.tsm.cards.repository.KnownWordRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = DEFINED_PORT)
@ActiveProfiles({ "it" })
public class DefinitionControllerIntegrationTest {

	private List<KnownWord> knownWords;

	@Autowired
	private KnownWordRepository knownWordRepository;

	// setUp
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
	public void test() {
	}
	
	@Test
	public void test2() {
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
