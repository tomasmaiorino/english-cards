package com.tsm.cards.resources;

import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_TYPE_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_TYPE_NAME;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class CardTypeResource extends BaseResource {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CARD_TYPE_NAME)
    @Size(min = 2, max = 30, message = INVALID_CARD_TYPE_NAME_SIZE)
    private String name;

}
