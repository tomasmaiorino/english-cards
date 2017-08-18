package com.tsm.cards.resources;

import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_IMG_URL_SIZE;
import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_STATUS;
import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_TYPE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_IMG_URL;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_NAME;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_STATUS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_TYPE;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class CardResource extends BaseResource {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CARD_NAME)
    @Size(min = 2, max = 30, message = INVALID_CARD_NAME_SIZE)
    private String name;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CARD_IMG_URL)
    @Size(min = 2, max = 150, message = INVALID_CARD_IMG_URL_SIZE)
    private String imgUrl;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CARD_STATUS)
    @Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_CARD_STATUS)
    private String status;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CARD_TYPE)
    @Min(value = 1, message = INVALID_CARD_TYPE)
    private Integer cardType;

}
