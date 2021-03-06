package com.tsm.cards.resources;

import com.tsm.cards.util.ContentConstants;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.tsm.cards.util.ErrorCodes.*;

public class ContentResource implements BaseResource {

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT_NAME)
	@Size(min = ContentConstants.CONTENT_MIN_NAME_SIZE, max = ContentConstants.CONTENT_MAX_NAME_SIZE, message = INVALID_CONTENT_NAME_SIZE)
	private String name;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT)
	@Size(min = ContentConstants.CONTENT_MIN_CONTENT_SIZE, max = ContentConstants.CONTENT_MAX_CONTENT_SIZE, message = INVALID_CONTENT_SIZE)
	private String content;

	@Getter
	@Setter
	private Boolean isHtml;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CONTENT_STATUS)
	@Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_CONTENT_STATUS)
	private String status;

	//
	@Getter
	@Setter
	private Integer contentType;

	//
	@Getter
	@Setter
	private String imgUrl;

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
		ContentResource other = (ContentResource) obj;
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