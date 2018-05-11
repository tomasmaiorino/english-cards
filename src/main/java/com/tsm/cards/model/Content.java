package com.tsm.cards.model;

import lombok.Getter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Table(name = "content")
public class Content extends BaseModel {

	private Content() {

	}

	@Id
	@GeneratedValue
	@Getter
	protected Integer id;

	public enum ContentStatus {
		ACTIVE, INACTIVE;
	}

	@Getter
	@Column(nullable = false)
	protected String name;

	@Getter
	protected String imgUrl;

	@Getter
	@Column(nullable = false, length = 6000)
	protected String content;

	@JoinColumn(name = "contentType_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Getter
	private ContentType contentType;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private ContentStatus status;

	@Getter
	@Column(name = "is_html", nullable = false)
	private Boolean isHtml = false;

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null!");
		this.name = name;
	}

	public void setImgUrl(final String imgUrl) {
		Assert.hasText(imgUrl, "The imgUrl must not be null!");
		this.imgUrl = imgUrl;
	}

	public void setStatus(final ContentStatus status) {
		Assert.notNull(status, "The contentStatus must not be null!");
		this.status = status;
	}

	public void setContent(final String content) {
		Assert.hasText(content, "The content must not be null nor empty!");
		this.content = content;
	}

	public void setContentType(final ContentType contentType) {
		Assert.notNull(contentType, "The contentType must not be null!");
		Assert.isTrue(contentType.getId() != null, "The contentType must not be new!");
		this.contentType = contentType;
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
		Content other = (Content) obj;
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

	public void setIsHtml(final Boolean isHtml) {
		Assert.notNull(isHtml, "The isHtml must not be null!");
		this.isHtml = isHtml;
	}

	public static class ContentBuilder {

		private final Content content;

		private ContentBuilder(final String name, final String content, final ContentType contentType,
				final ContentStatus status) {
			this.content = new Content();
			this.content.setName(name);
			this.content.setContent(content);
			this.content.setContentType(contentType);
			this.content.setStatus(status);
		}

		public static ContentBuilder Content(final String name, final String content, final ContentType contentType,
				final ContentStatus status) {
			return new ContentBuilder(name, content, contentType, status);
		}

		public ContentBuilder isHtml(final Boolean isHtml) {
			this.content.setIsHtml(isHtml);
			return this;
		}

		public ContentBuilder imgUrl(final String imgUrl) {
			this.content.setImgUrl(imgUrl);
			return this;
		}

		public Content build() {
			return content;
		}
	}

}
