package com.tsm.cards.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "content_type_rule")
public class ContentTypeRule extends BaseModel {

	private ContentTypeRule() {
	}

	public enum ContentTypeStatus {
		ACTIVE, INACTIVE;
	}

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	@Getter
	@Column(nullable = false, length = 100)
	private String attribute;

	@Getter
	private String rule;

	@Getter
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "contentType_id")
	private ContentType contentType;

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
		ContentTypeRule other = (ContentTypeRule) obj;
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

	public void setAttribute(final String attribute) {
		Assert.hasText(attribute, "The attribute must not be null nor empty!");
		this.attribute = attribute;
	}

	public void setRule(final String rule) {
		Assert.hasText(rule, "The rule must not be null nor empty!");
		this.rule = rule;
	}

	public void setContentType(final ContentType contentType) {
		Assert.notNull(contentType, "The contentType must not be null!");
		this.contentType = contentType;
	}

	public static class ContentTypeRuleBuilder {

		private final ContentTypeRule contentTypeRule;

		private ContentTypeRuleBuilder(final String attribute, final String rule, final ContentType contentType) {
			contentTypeRule = new ContentTypeRule();
			contentTypeRule.setAttribute(attribute);
			contentTypeRule.setRule(rule);
			contentTypeRule.setContentType(contentType);
		}

		public static ContentTypeRuleBuilder ContentTypeRule(final String attribute, final String rule,
				final ContentType contentType) {
			return new ContentTypeRuleBuilder(attribute, rule, contentType);
		}

		public ContentTypeRule build() {
			return contentTypeRule;
		}
	}
}
