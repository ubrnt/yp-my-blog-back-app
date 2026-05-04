package ru.practicum.blog.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;

public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestId = RandomStringUtils.randomAlphanumeric(8);
        MDC.put("requestId", requestId);
        MDC.put("requestInfo", req.getMethod() + " " + req.getRequestURI());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
