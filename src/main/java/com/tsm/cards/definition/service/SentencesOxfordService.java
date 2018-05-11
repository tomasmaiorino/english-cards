package com.tsm.cards.definition.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.documents.SentencesDefinition;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class SentencesOxfordService extends OxfordService<SentencesDefinition> {

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
    @Value("${oxford.service.sentences.service.endpoint}")
    protected String oxfordServiceSentencesServiceEndpoint;

    @Override
    protected String getServiceName() {
        return OxfordService.SENTENCES;
    }

    @Override
    protected SentencesDefinition createDefinition(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(new InputStreamReader(entity.getContent()), SentencesDefinition.class);
    }

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
        return getOxfordServiceSentencesServiceEndpoint().replace("X", word);
    }
}
