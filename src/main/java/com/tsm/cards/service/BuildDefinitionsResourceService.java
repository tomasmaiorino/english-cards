package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.Definition;
import com.tsm.cards.model.Entries;
import com.tsm.cards.model.Results;
import com.tsm.cards.resources.DefinitionResource;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildDefinitionsResourceService {

	@Value("${maxDefinitionListSize}")
	@Getter
	@Setter
	private Integer maxDefinitionListSize;

	// TODO refactor these methods
	public List<DefinitionResource> loadResource(List<Definition> cachedWords) {
		Assert.notEmpty(cachedWords, "The cachedWords must not be empty.");
		log.info("Loading resource ->");
		List<DefinitionResource> resources = new ArrayList<>();

		List<Results> results = cachedWords.stream().flatMap(c -> c.getResults().stream())
				.filter(r -> r.getLexicalEntries() != null).collect(Collectors.toList());

		results.forEach(r -> {
			Map<String, String> definitions = new HashMap<>();
			DefinitionResource resource = new DefinitionResource();
			r.getLexicalEntries().forEach(l -> {
				resource.setWord(r.getId());
				if (l.getEntries() != null && !l.getEntries().isEmpty()) {
					l.getEntries().forEach(e -> {
						createDefinitions(e, definitions);
					});
				}
			});

			resource.setDefinitions(definitions);
			resources.add(resource);
		});
		log.info("Loading resource <-");
		return resources;
	}

	private void createDefinitions(Entries e, Map<String, String> definitions) {
		if (e.getSenses() != null && !e.getSenses().isEmpty()) {
			e.getSenses().forEach(s -> {
				if (s.getDefinitions() != null && !s.getDefinitions().isEmpty()) {
					addDefinition(definitions, s.getId(), s.getDefinitions());
				}
				if (s.getSubsenses() != null && !s.getSubsenses().isEmpty()) {
					s.getSubsenses().forEach(ss -> {
						addDefinition(definitions, ss.getId(), ss.getDefinitions());
					});
				}
			});
		}
	}

	private void addDefinition(Map<String, String> definitions, String id, List<String> content) {
		if (definitions.size() == maxDefinitionListSize || content == null || content.isEmpty()) {
			return;
		}
		definitions.put(id, content.get(0));
	}
}
