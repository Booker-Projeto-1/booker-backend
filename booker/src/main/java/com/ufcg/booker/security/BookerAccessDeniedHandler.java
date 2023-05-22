package com.ufcg.booker.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
public class BookerAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = getLogger(BookerAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("[BookerAccessDeniedHandler] Access Denied. URL = {}", request.getRequestURL());

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write("{\"error\": \"Access Denied\"}");
    }
}
