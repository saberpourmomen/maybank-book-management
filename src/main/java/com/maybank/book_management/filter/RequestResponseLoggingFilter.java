package com.maybank.book_management.filter;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger("com.maybank.book_management.filter");

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Set<String> SENSITIVE_FIELDS =
            Set.of("password", "token", "email", "phone", "address");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request, 1024 * 1024);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);

        chain.doFilter(req, res);

        logRequest(req);
        logResponse(res);

        res.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String requestBody = getBodyAsString(request.getContentAsByteArray());
        requestBody = maskSensitiveData(requestBody);

        logger.info("REQUEST {} {} body={}",
                request.getMethod(),
                request.getRequestURI(),
                requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String responseBody = getBodyAsString(response.getContentAsByteArray());
        responseBody = maskSensitiveData(responseBody);

        logger.info("RESPONSE status={} body={}",
                response.getStatus(),
                responseBody);
    }

    private String getBodyAsString(byte[] body) {
        if (body == null || body.length == 0) return "";
        return new String(body, StandardCharsets.UTF_8);
    }

    private String maskSensitiveData(String body) {
        if (body == null || body.isEmpty()) return body;

        try {
            JsonNode root = mapper.readTree(body);

            maskNode(root);
            return root.toString();

        } catch (Exception e) {
            return body;
        }
    }

    private void maskNode(JsonNode node) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                if (SENSITIVE_FIELDS.contains(entry.getKey())) {
                    ((ObjectNode) node)
                            .put(entry.getKey(), "****");
                } else {
                    maskNode(entry.getValue());
                }
            });
        } else if (node.isArray()) {
            node.forEach(this::maskNode);
        }
    }
}