package com.tsm.cards.resources;

import static com.tsm.cards.util.ErrorCodes.INVALID_CONTENT_TYPE_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.INVALID_CONTENT_TYPE_STATUS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CONTENT_TYPE_NAME;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CONTENT_TYPE_STATUS;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.tsm.cards.util.ContentTypeConstants;

import lombok.Getter;
import lombok.Setter;

public class ContentTypeResource implements BaseResource {

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT_TYPE_NAME)
	@Size(min = ContentTypeConstants.CONTENT_TYPE_MIN_NAME_SIZE, max = ContentTypeConstants.CONTENT_TYPE_MAX_NAME_SIZE, message = INVALID_CONTENT_TYPE_NAME_SIZE)
	private String name;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT_TYPE_STATUS)
	@Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_CONTENT_TYPE_STATUS)
	private String status;
	//
	@Getter
	private Set<ContentTypeRuleResource> rules;

	@Getter
	@Setter
	private Set<ContentResource> contents;

	//
	@Getter
	@Setter
	private String imgUrl;

	public void setRules(final Set<ContentTypeRuleResource> rules) {
		this.rules = rules;
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
		ContentTypeResource other = (ContentTypeResource) obj;
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