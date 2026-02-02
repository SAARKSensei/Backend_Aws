package com.sensei.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_SIZE = 3000;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {

            long duration = System.currentTimeMillis() - start;

            String requestBody = extract(wrappedRequest.getContentAsByteArray());
            String responseBody = extract(wrappedResponse.getContentAsByteArray());

            log.info(
                "HTTP_IN_OUT method={} path={} status={} durationMs={} requestBody={} responseBody={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration,
                requestBody,
                responseBody
            );

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String extract(byte[] content) {
        if (content == null || content.length == 0) return null;
        String body = new String(content, StandardCharsets.UTF_8);
        return body.length() > MAX_BODY_SIZE
                ? body.substring(0, MAX_BODY_SIZE) + "...(truncated)"
                : body;
    }
}
