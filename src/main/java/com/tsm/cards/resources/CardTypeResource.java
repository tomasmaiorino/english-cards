package com.tsm.cards.resources;

import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_TYPE_STATUS;
import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_TYPE_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_TYPE_STATUS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_TYPE_NAME;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.util.CollectionUtils;

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
    
    @Getter
    @Setter
    @NotNull(message = REQUIRED_CARD_TYPE_STATUS)
    @Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_CARD_TYPE_STATUS)
    private String status;

    @Getter
    @Setter
    private Set<CardResource> cards;

    @Getter
    @Setter
    private String imgUrl;

    public void addCards(final CardResource cardResource) {
        if (CollectionUtils.isEmpty(cards)) {
            cards = new HashSet<>();
        }
        cards.add(cardResource);
    }

}
