package com.tsm.cards.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
//@Profile(value = "local")
@Slf4j
public class InitiaLocallLoad implements ApplicationListener<ApplicationReadyEvent> {

	private static final String EMAIL_SERVICE_ENDPOINT = "/api/v1/*";

	protected static final String COMMA_SEPARATOR = ",";

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				log.info("Loading allowedOrigins permissions ->");

				registry.addMapping(EMAIL_SERVICE_ENDPOINT).allowedOrigins("*");

				log.info("Loading allowedOrigins permissions <-");
			}
		};
	}

}
