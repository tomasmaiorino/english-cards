package com.tsm.cards.documents;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseDefinition {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private List<Results> results;

    @Getter
    @Setter
    private Metadata metadata;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public List<Senses> getSenses() {
        List<Senses> senses = new ArrayList<>();
        this.getResults().forEach(r -> {
            if (!CollectionUtils.isEmpty(r.getLexicalEntries())) {
                r.getLexicalEntries().forEach(l -> {
                    if (!CollectionUtils.isEmpty(l.getEntries())) {
                        l.getEntries().forEach(s -> {
                            if (!CollectionUtils.isEmpty(s.getSenses())) {
                                senses.addAll(s.getSenses());
                                return;
                            }
                        });
                    }
                });
            }
        });
        return CollectionUtils.isEmpty(senses) ? null : senses;
    }

}
