package com.tsm.cards.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "contentType")
	@Getter
	@Setter
	@JsonBackReference
	private Set<Content> contents;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "contentType")
	@Getter
	@JsonBackReference
	private Set<ContentTypeRule> rules;

	public void setImgUrl(final String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null!");
		this.name = name;
	}

	public void setStatus(final ContentTypeStatus status) {
		Assert.notNull(status, "The status must not be null!");
		this.status = status;
	}

	public void setRules(final Set<ContentTypeRule> rules) {
		// clean rules list
		if (!CollectionUtils.isEmpty(this.getRules())) {
			this.getRules().clear();
		}
		if (!CollectionUtils.isEmpty(rules)) {
			this.rules.addAll(rules);
		}
	}

	public void addRule(final ContentTypeRule rule) {
		Assert.notNull(rule, "The rule must not be null!");
		if (rules == null || rules.isEmpty()) {
			rules = new HashSet<>();
		}
		rules.add(rule);
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

	public static class ContentTypeBuilder {

		private final ContentType contentType;

		private ContentTypeBuilder(final String name) {
			contentType = new ContentType();
			contentType.setName(name);
		}

		public static ContentTypeBuilder ContentType(final String name) {
			return new ContentTypeBuilder(name);
		}

		public ContentTypeBuilder status(final ContentTypeStatus status) {
			contentType.setStatus(status);
			return this;
		}

		public ContentTypeBuilder imgUrl(final String imgUrl) {
			contentType.setImgUrl(imgUrl);
			return this;
		}

		public ContentType build() {
			return contentType;
		}
	}
}