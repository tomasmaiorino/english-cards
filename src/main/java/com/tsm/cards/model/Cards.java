package com.tsm.cards.model;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "cards")
@TypeAlias("cards")
public class Cards {

	@Getter
	@Setter
	private List<String> definitionsId;

	@Getter
	@Setter
	private String word;

}
