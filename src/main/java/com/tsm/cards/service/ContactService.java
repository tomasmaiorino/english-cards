package com.tsm.cards.service;


import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tsm.cards.resources.ContactResource;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by tomas on 6/10/18.
 */
@Slf4j
@Service
public class ContactService {

	@Value("${contact.application.url}")
	private String contactApplicationUrl;

	@Value("${englishCardClientToken}")
	private String englishCardClientToken;

	@Value("${contactServiceTimeout}")
	private int contactServiceTimeout;

	@Autowired
	private RestTemplate restTemplate;

	public boolean sendContactMessage(final ContactResource contact) {
		Assert.notNull(contact, "The contact must not be null!");
		log.info("Sending contact message [{}].");

		HttpEntity<ContactResource> request = new HttpEntity<>(contact);
		restTemplate.setRequestFactory(getClientHttpRequestFactory());

		ResponseEntity<ContactResource> exchange = this.restTemplate.exchange(contactApplicationUrl, HttpMethod.POST,
				request, new ParameterizedTypeReference<ContactResource>() {
				}, englishCardClientToken);

		log.info("Sending contact message response HTTP [{}]. ", exchange.getStatusCodeValue());
		log.debug("Message response [{}].", exchange);
		if (exchange.getStatusCodeValue() != HttpStatus.SC_OK) {
		}
		return exchange.getStatusCodeValue() == HttpStatus.SC_OK;
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = contactServiceTimeout;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}

}
