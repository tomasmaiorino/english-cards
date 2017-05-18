package com.tsm.cards.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "track_word")
@TypeAlias("track_word")
public class TrackWord {

	@Getter
	@Setter
	private String id;
	
	@Getter
	@Setter
	private Long callCount;
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
