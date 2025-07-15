package com.dre.gymapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionLoggingFilter extends OncePerRequestFilter {
    // Header name for transaction ID
    private static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get transaction ID from header or generate new one if not present
        String transactionId = request.getHeader(TRANSACTION_ID_HEADER);
        if (transactionId == null || transactionId.isBlank()) {
            transactionId = UUID.randomUUID().toString();
        }

        // Add transaction ID to MDC for logging
        MDC.put("transactionId", transactionId);
        // Add transaction ID to request attributes
        request.setAttribute(TRANSACTION_ID_HEADER, transactionId);
        // Add transaction ID to the response header
        response.addHeader(TRANSACTION_ID_HEADER, transactionId);

        try {
            // Continue filter chain execution
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC
            MDC.remove(transactionId);
        }

    }
}