package com.tsm.cards.resources;

import static com.tsm.cards.util.ErrorCodes.INVALID_CONTENT_TYPE_STATUS;
import static com.tsm.cards.util.ErrorCodes.INVALID_CONTENT_TYPE_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CONTENT_TYPE_STATUS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CONTENT_TYPE_NAME;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class ContentTypeResource extends BaseResource {

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT_TYPE_NAME)
	@Size(min = 2, max = 30, message = INVALID_CONTENT_TYPE_NAME_SIZE)
	private String name;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT_TYPE_STATUS)
	@Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_CONTENT_TYPE_STATUS)
	private String status;
	//
	// @Getter
	// @Setter
	// private Set<CardResource> cards;
	//
	@Getter
	@Setter
	private String imgUrl;
	//
	// public void addCards(final CardResource cardResource) {
	// if (CollectionUtils.isEmpty(cards)) {
	// cards = new HashSet<>();
	// }
	// cards.add(cardResource);
	// }

}
