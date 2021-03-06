package com.tsm.cards.service;

import com.tsm.cards.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Profile(value = "prod")
@Component
@Slf4j
public class CORSFilter implements Filter {

	@Autowired
	private ClientService clientService;

	@Value(value = "${client.service.url}")
	private String clientServiceUrl;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		if (!request.getRequestURI().contains(clientServiceUrl)) {
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
				response.setHeader("Access-Control-Expose-Headers", "Location");
		} else {
			log.debug("Skiping host verification");
		}

		chain.doFilter(req, res);
	}

	private String getClientHost(final HttpServletRequest req) {
		String host = null;
		String token = "";
		try {
			String request = req.getRequestURI();
			log.debug("parsing request [{}].", request);

			token = request.substring(request.lastIndexOf("/") + 1, request.length());
			log.info("locking for a client with the token [{}].", token);

			Client client = clientService.findByToken(token);
			host = client.getClientHosts().iterator().next().getHost();

		} catch (Exception e) {
			log.info("Client not found [{}].", token);
		}
		return host;
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
