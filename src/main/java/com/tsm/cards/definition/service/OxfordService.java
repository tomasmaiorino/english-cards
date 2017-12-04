package com.tsm.cards.definition.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.util.Assert;

import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.exceptions.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OxfordService<T extends BaseDefinition> {

    public static final String SENTENCES = "sentence";

    public static final String SYNONYMS = "synonyms";

    public static final String DEFINITIONS = "definitions";

    protected abstract String getServiceName();

    protected abstract String getServiceApiId();

    protected abstract String getServiceApiKey();

    protected abstract String getServiceEndpoint(final String word);

    protected abstract T createDefinition(final HttpResponse response) throws IOException;

    public T find(final String word) throws Exception {
    	Assert.hasText(word, "The word must not be null or empty!");
        log.info("Calling [{}] for the workd [{}]  :0 ", getServiceName(), word);

        HttpGet getRequest = initialConfig(word);

        try {

            HttpResponse response = doesRequest(getRequest, word);
            return createDefinition(response);

        } catch (IOException e) {
            log.error(String.format("Error trying to recover the [%s] to the word [%s].", getServiceName(), word), e);
            throw new Exception(String.format("Error trying to recover the s[%s] to the word [%s]. ", getServiceName(), word));
        }
    }

    private HttpGet initialConfig(final String word) {
        String endpoint = getServiceEndpoint(word);
        HttpGet getRequest = new HttpGet(endpoint);
        getRequest.addHeader("app_id", getServiceApiId());
        getRequest.addHeader("app_key", getServiceApiKey());

        log.info("Initial config:");
        log.info("Endpoint [{}], app id [{}], app key [{}].", endpoint, getServiceApiId(), getServiceApiKey());
        return getRequest;
    }

    private HttpResponse doesRequest(HttpGet getRequest, final String word) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(getRequest);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            treatNotSuccess(response, word);
        }
        return response;
    }

    private void treatNotSuccess(final HttpResponse response, final String word) throws Exception {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
            throw new ResourceNotFoundException(getServiceName() + "NotFound");
        } else {
            throw new Exception(String.format("Oxford service internal error [%s]", response.getStatusLine().getStatusCode()));
        }
    }
}
