package com.tsm.builders;

import com.tsm.cards.resources.DefinitionResource;

import java.util.Map;
import java.util.Set;

public class DefinitionResourceBuilder {

	private DefinitionResource resource = new DefinitionResource();

	public DefinitionResourceBuilder words(final Set<String> words) {
		resource.setWords(words);
		return this;
	}

	public DefinitionResourceBuilder definitions(final Map<String, String> definitions) {
		resource.setDefinitions(definitions);
		return this;
	}

	public DefinitionResourceBuilder word(final String word) {
		resource.setWord(word);
		return this;
	}

	public DefinitionResource build() {
		return resource;
	}

}
