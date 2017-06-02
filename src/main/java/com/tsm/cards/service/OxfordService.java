package com.tsm.cards.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OxfordService {

	@Getter
	@Setter
	@Value("${oxford.service.api.id}")
	private String oxfordServiceApiId;

	@Getter
	@Setter
	@Value("${oxford.service.app.key}")
	private String oxfordServiceAppkey;

	@Getter
	@Setter
	@Value("${oxford.service.definitions.service.endpoint}")
	private String oxfordServiceDefinitionsServiceEndpoint;

	private HttpGet initialConfig(final String word) {
		HttpGet getRequest = new HttpGet(getOxfordServiceDefinitionsServiceEndpoint().replace("X", word));
		getRequest.addHeader("accept", "application/json");
		getRequest.addHeader("app_id", getOxfordServiceApiId());
		getRequest.addHeader("app_key", getOxfordServiceAppkey());
		return getRequest;
	}

	public Definition findWordDefinition(final String word) throws Exception {
		Assert.notNull(word, "The word must not be null.");

		log.info("Looking for the [{}] definition :0 ", word);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = initialConfig(word);

		try {

			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				treatNotSuccess(response, word);
			}

			return createDefinition(response);

		} catch (IOException e) {
			log.error("Error trying to recover the definition to the word %s ", word);
			throw new Exception("Error trying to recover the definition to the word " + word);
		}
	}

	private Definition createDefinition(final HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.fromJson(new InputStreamReader(entity.getContent()), Definition.class);
	}

	private void treatNotSuccess(final HttpResponse response, final String word) throws Exception {
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
			throw new ResourceNotFoundException(
					String.format("Definition not found for the word [%s], error code [%s] ", word,
							response.getStatusLine().getStatusCode()));
		} else {
			throw new Exception("Oxford service internal error");
		}
	}

}
