package com.tsm.cards.definition.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.documents.Definition;

import lombok.Getter;
import lombok.Setter;

@Service
public class DefinitionOxfordService extends OxfordService<Definition> {

    @Value("${oxford.service.api.id}")
    @Getter
    @Setter
    protected String oxfordServiceApiId;

    @Value("${oxford.service.app.key}")
    @Getter
    @Setter
    protected String oxfordServiceAppkey;

    @Getter
    @Setter
    @Value("${oxford.service.definitions.service.endpoint}")
    protected String oxfordServiceDefinitionsServiceEndpoint;

    @Override
    protected String getServiceApiId() {
        return oxfordServiceApiId;
    }

    @Override
    protected String getServiceApiKey() {
        return oxfordServiceAppkey;
    }

    @Override
    protected String getServiceEndpoint(String word) {
        return getOxfordServiceDefinitionsServiceEndpoint().replace("X", word);
    }

    @Override
    protected String getServiceName() {
        return OxfordService.DEFINITIONS;
    }

    @Override
    protected Definition createDefinition(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(new InputStreamReader(entity.getContent()), Definition.class);
    }

}
