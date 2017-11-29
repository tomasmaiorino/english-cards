package com.tsm.cards.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;

@Entity
@Table(name = "card_type")
public class CardType extends BaseModel {

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	@Getter
	@Column(nullable = false, length = 100)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cardType")
	@JsonBackReference
	@Getter
	private Set<Card> cards;

	@Getter
	private String imgUrl;

	public void setImgUrl(final String imgUrl) {
		Assert.hasText(imgUrl, "The imgUrl must not be null!");
		this.imgUrl = imgUrl;
	}

	public void setCards(final Set<Card> cards) {
		Assert.notEmpty(cards, "The cards must not be empty!");
		this.cards = cards;
	}

	public void addCard(final Card card) {
		Assert.notNull(card, "The card must not be null!");
		if (cards == null) {
			cards = new HashSet<Card>();
		}
		cards.add(card);
	}

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null!");
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		CardType other = (CardType) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
