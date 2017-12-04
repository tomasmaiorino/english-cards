package com.tsm.cards.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tsm.cards.service.AssertClientRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PostCallsInterceptor extends HandlerInterceptorAdapter {

    protected static final String ADMIN_TOKEN_HEADER = "AT";

    protected static final String COMMA_SEPARATOR = ",";

    @Autowired
    private AssertClientRequest assertClientRequest;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("preHandle ->");
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            log.debug("Checking POST or PUT call. Method [{}], admin header [{}].", request.getMethod(),
                request.getHeader(ADMIN_TOKEN_HEADER));
            assertClientRequest.assertClientHeader(request.getHeader(ADMIN_TOKEN_HEADER));
        }
        log.debug("preHandle <-");
        return true;
    }

}
