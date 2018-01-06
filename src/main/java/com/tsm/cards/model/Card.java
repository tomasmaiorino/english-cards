package com.tsm.cards.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "card")
public class Card extends BaseModel {

	private Card() {
	}

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	public enum CardStatus {
		ACTIVE, INACTIVE;
	}

	@JoinColumn(name = "card_type")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Getter
	private CardType cardType;

	@Getter
	@Column(nullable = false)
	private String name;

	@Getter
	@Column(nullable = false)
	private String imgUrl;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private CardStatus status;

	public void setCardType(final CardType cardType) {
		Assert.notNull(cardType, "The cardType must not be null!");
		this.cardType = cardType;
	}

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null!");
		this.name = name;
	}

	public void setImgUrl(final String imgUrl) {
		Assert.hasText(imgUrl, "The imgUrl must not be null!");
		this.imgUrl = imgUrl;
	}

	public void setCardStatus(final CardStatus status) {
		Assert.notNull(status, "The status must not be null!");
		this.status = status;
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
		Card other = (Card) obj;
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

	public static class CardBuilder {

		private final Card card;

		private CardBuilder(final String name, final CardType cardType, final CardStatus status, final String imgUrl) {
			card = new Card();
			card.setCardStatus(status);
			card.setCardType(cardType);
			card.setImgUrl(imgUrl);
			card.setName(name);
		}

		public static CardBuilder Card(final String name, final CardType cardType, final CardStatus status,
				final String imgUrl) {
			return new CardBuilder(name, cardType, status, imgUrl);
		}

		public Card build() {
			return card;
		}
	}
}