package com.tsm.cards.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.cards.model.Definition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessDefinitionsService {

	private static final String WORDS_SEPARATOR = ",";

	@Autowired
	@Getter
	@Setter
	private DefinitionService definitionService;

	public List<Definition> getDefinitionByDefinitionsIds(final Set<String> definitionsIds) {
		Assert.notEmpty(definitionsIds, "The definitionsIds must not be empty.");
		log.info("get original call by definitionsIds [{}]", definitionsIds);

		List<Definition> calls = new ArrayList<>();
		definitionsIds.forEach(w -> {
			Definition call = definitionService.findByDefinitionId(w);
			if (call != null) {
				calls.add(call);
			} else {
				log.info("Original call not found for definition id [{}]", w);
			}
		});

		log.info("original calls found [{}]", calls.size());
		return calls;
	}

	public Set<String> splitDefinitionsIds(final String definitionsIds) {
		Assert.notNull(definitionsIds, "The words must not be empty.");
		log.info("spliting definitions[{}]", definitionsIds);
		if (definitionsIds.contains(WORDS_SEPARATOR)) {
			List<String> result = createDefinitionsIdsList(definitionsIds);
			return new HashSet<>(result);
		} else {
			return new HashSet<>(Collections.singletonList(definitionsIds));
		}
	}

	private List<String> createDefinitionsIdsList(final String words) {
		String[] content = words.split(WORDS_SEPARATOR);
		List<String> result = new ArrayList<>();
		for (String s : content) {
			result.add(s);
		}
		return result;
	}
}
