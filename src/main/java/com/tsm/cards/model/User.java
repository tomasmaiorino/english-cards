package com.tsm.cards.model;

import javax.persistence.Id;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "user")
@TypeAlias("user")
public class User extends BaseModel {

    @Getter
    @Setter
    @Id
    private Long id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String name;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
