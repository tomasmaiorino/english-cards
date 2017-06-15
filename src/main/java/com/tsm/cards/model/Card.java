package com.tsm.cards.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "card")
@TypeAlias("card")
public class Card extends BaseModel {

	@Getter
	@Setter
	@NotNull(message = "word required.")
	private String word;

	@Getter
	@Setter
	@NotNull(message = "image url required.")
	private String imageUrl;

	@Getter
	@Setter
	private String type = "word";

}
