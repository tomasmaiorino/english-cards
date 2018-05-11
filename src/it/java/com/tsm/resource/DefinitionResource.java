package com.tsm.resource;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Map;
import java.util.Set;

public class DefinitionResource {

    public static DefinitionResource build() {
        return new DefinitionResource();
    }

    private DefinitionResource() {
    }

    @Getter
    @Setter
    private String word;

    @Getter
    @Setter
    private Set<String> words;

    @Getter
    @Setter
    private Map<String, String> definitions;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
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
        DefinitionResource other = (DefinitionResource) obj;
        if (word == null || other.word == null) {
            return false;
        }
        return word.equals(other.word);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(word)
            .toHashCode();
    }
}
