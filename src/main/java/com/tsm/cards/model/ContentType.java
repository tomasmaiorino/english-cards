package com.tsm.cards.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "content_type")
public class ContentType extends BaseModel {

	public enum ContentTypeStatus {
		ACTIVE, INACTIVE;
	}

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	@Getter
	@Column(nullable = false, length = 100)
	private String name;

	@Getter
	private String imgUrl;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private ContentTypeStatus status;

	public void setImgUrl(final String imgUrl) {
		Assert.hasText(imgUrl, "The imgUrl must not be null!");
		this.imgUrl = imgUrl;
	}

	// public void setCards(final Set<Card> cards) {
	// Assert.notEmpty(cards, "The cards must not be empty!");
	// this.cards = cards;
	// }
	//
	// public void addCard(final Card card) {
	// Assert.notNull(card, "The card must not be null!");
	// if (cards == null) {
	// cards = new HashSet<Card>();
	// }
	// cards.add(card);
	// }

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null!");
		this.name = name;
	}

	public void setStatus(final ContentTypeStatus status) {
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
		ContentType other = (ContentType) obj;
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
