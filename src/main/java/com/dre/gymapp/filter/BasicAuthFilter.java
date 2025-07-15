package com.dre.gymapp.filter;


import com.dre.gymapp.exception.UnauthorizedException;
import com.dre.gymapp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {

    private UserService userService;

    // Setter injection for UserService
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        // Skip authentication for auth endpoints
        if (path.contains("api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Get Authorization header
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
                String base64credentials = authorizationHeader.substring("Basic ".length());
                // Decode base64 credentials
                byte[] decodedBytes = Base64.getDecoder().decode(base64credentials);
                // Convert decoded bytes to string (username:password format)
                String credentials = new String(decodedBytes, StandardCharsets.UTF_8);  //username:password
                String[] values = credentials.split(":", 2);
                if (values.length != 2) {
                    throw new UnauthorizedException("Invalid Authorization format");
                }
                String username = values[0];
                String password = values[1];

                // Authenticate a user with credentials
                userService.authenticateUser(username, password);
            } else {
                throw new UnauthorizedException("Missing Authorization header");
            }
            // Continue filter chain if authentication successful
            filterChain.doFilter(request, response);

        } catch (UnauthorizedException e) {
            // Set error response for unauthorized access
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().println(e.getMessage());
        }
    }
}
