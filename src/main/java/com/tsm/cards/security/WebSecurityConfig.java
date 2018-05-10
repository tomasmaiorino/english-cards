package com.tsm.cards.security;

import static com.tsm.cards.security.SecurityConstants.*;
import static com.tsm.cards.security.SecurityConstants.CARDS_URL;
import static com.tsm.cards.security.SecurityConstants.DEFINITIONS_URL;
import static com.tsm.cards.security.SecurityConstants.SIGN_UP_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	public JWTAuthorizationFilter jwtAuthorizationFilter() {
		return new JWTAuthorizationFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
		.antMatchers(HttpMethod.GET, CARDS_URL).permitAll()
		.antMatchers(HttpMethod.GET, CARDS_TYPE_URL).permitAll()
		.antMatchers(HttpMethod.GET, CONTENTS_URL).permitAll()
		.antMatchers(HttpMethod.GET, DEFINITIONS_URL).permitAll()
		.antMatchers(HttpMethod.GET, CONTENT_TYPE_URL).permitAll()		
		.anyRequest().authenticated().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

}
