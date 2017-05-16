package com.tsm.cards.model;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class BaseModel {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    public final LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @JsonIgnore
    public boolean isNew() {
        return created == null;
    }

    @PrePersist
    public final void prePersist() {
        created = LocalDateTime.now();
    }

    @PreUpdate
    public final void preUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        // return ReflectionToStringBuilder.toString(this);
        return null;
    }

}
