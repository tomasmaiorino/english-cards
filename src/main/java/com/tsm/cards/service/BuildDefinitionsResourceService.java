package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.Entries;
import com.tsm.cards.model.Definition;
import com.tsm.cards.model.Results;
import com.tsm.cards.resources.DefinitionResource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildDefinitionsResourceService {

	public List<DefinitionResource> loadResource(List<Definition> cachedWords) {
		Assert.notEmpty(cachedWords, "The cachedWords must not be empty.");
		log.info("Loading resource ->");
		List<DefinitionResource> resources = new ArrayList<>();

		cachedWords.forEach(o -> {
			Map<String, String> definitions = new HashMap<>();
			DefinitionResource resource = new DefinitionResource();
			o.getResults().forEach(r -> {
				r.getLexicalEntries().forEach(l -> {
					resource.setWord(r.getId());
					l.getEntries().forEach(e -> {
						createDefinitions(e, definitions);
					});
				});
			});
			resource.setDefinitions(definitions);
			resources.add(resource);
		});
		log.info("Loading resource <-");
		return resources;
	}

	private void createDefinitions(Entries e, Map<String, String> definitions) {
		e.getSenses().forEach(s -> {
			definitions.put(s.getId(), s.getDefinitions().get(0));
			if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
				s.getSubsenses().forEach(ss -> {
					definitions.put(ss.getId(), ss.getDefinitions().get(0));
				});
			}
		});
	}

}
