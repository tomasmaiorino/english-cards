package com.tsm.cards.definition.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.documents.SynonymsDefinition;

import lombok.Getter;
import lombok.Setter;

@Service
public class SynonymsOxfordService extends OxfordService<SynonymsDefinition> {

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
    @Value("${oxford.service.synonyms.service.endpoint}")
    protected String oxfordServiceSynonymsServiceEndpoint;

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
        return getOxfordServiceSynonymsServiceEndpoint().replace("X", word);
    }

    @Override
    protected String getServiceName() {
        return OxfordService.SYNONYMS;
    }

    @Override
    protected SynonymsDefinition createDefinition(final HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(new InputStreamReader(entity.getContent()), SynonymsDefinition.class);
    }

}
