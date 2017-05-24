package com.tsm.builders;

import java.util.Map;
import java.util.Set;

import com.tsm.cards.resources.DefinitionsResource;

public class DefinitionResourceBuilder {

	private DefinitionsResource resource = new DefinitionsResource();

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

	public DefinitionsResource build() {
		return resource;
	}

}
