package com.tsm.cards.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.Setter;

@TypeAlias("subsenses")
public class Subsenses {

	@Getter
	@Setter
	private List<String> definitions;

	@Getter
	@Setter
	private String id;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
