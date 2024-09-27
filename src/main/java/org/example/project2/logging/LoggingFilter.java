package org.example.project2.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append(", ");
        }
        return headers.toString();
    }

    private String getResponseHeaders(HttpServletResponse response) {
        StringBuilder headers = new StringBuilder();
        response.getHeaderNames().forEach(headerName -> {
            String headerValue = response.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append(", ");
        });
        return headers.toString();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());

        String requestHeaders = getHeaders(request);
        String responseHeaders = getResponseHeaders(responseWrapper);

        LOGGER.info(
                "FINISHED PROCESSING : METHOD={}; REQUESTURI={}; REQUEST HEADERS={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE HEADERS={}; RESPONSE={}; TIME TAKEN={}",
                request.getMethod(), request.getRequestURI(), requestHeaders, requestBody, response.getStatus(),
                responseHeaders, responseBody, timeTaken);

        responseWrapper.copyBodyToResponse();
    }
}
