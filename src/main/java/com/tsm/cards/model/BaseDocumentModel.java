package com.tsm.cards.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

public class BaseDocumentModel {

    @Id
    @Getter
    @Setter
    private String id;

    @CreatedDate
    @Getter
    private LocalDateTime created;

    @LastModifiedDate
    @Getter
    private LocalDateTime lastUpdated;

    @JsonIgnore
    public boolean isNew() {
        return created == null;
    }

    @Override
    public String toString() {
        // return ReflectionToStringBuilder.toString(this);
        return null;
    }

}
