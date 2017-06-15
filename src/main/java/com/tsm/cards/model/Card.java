package com.tsm.cards.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tsm.cards.exceptions.ErrorsCode;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "card")
@TypeAlias("card")
public class Card extends BaseModel {

	@Getter
	@Setter
	@NotNull(message = ErrorsCode.FIELD_REQUIRED)
	private String word;

	@Getter
	@Setter
	@NotNull(message = ErrorsCode.FIELD_REQUIRED)
	private String imageUrl;

	@Getter
	@Setter
	private String type = "word";

}
