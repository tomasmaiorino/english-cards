package com.tsm.cards.documents;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "definition")
@TypeAlias("definition")
public class Definition extends BaseDefinition {

}
