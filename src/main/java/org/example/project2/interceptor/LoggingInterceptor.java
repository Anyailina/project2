package org.example.project2.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Enumeration;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logRequestDetails(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        logResponseDetails(response);
    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("HTTP Method: {}", request.getMethod());
        logger.info("Request Headers:");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("{}: {}", headerName, request.getHeader(headerName));
        }

        //StringBuilder requestBody = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//        }
//
//        if (!requestBody.isEmpty()) {
//            logger.info("Request Body: {}", requestBody);
//        } else {
//            logger.info("Request Body is empty.");
//        }
    }

    private void logResponseDetails(HttpServletResponse response) {
        logger.info("Response Status: {}", response.getStatus());
        logger.info("Response Headers:");

        for (String headerName : response.getHeaderNames()) {
            logger.info("{}: {}", headerName, response.getHeader(headerName));
        }
    }
}