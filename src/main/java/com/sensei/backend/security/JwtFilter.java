package com.sensei.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // ✅ 1. Allow preflight (CORS fix - VERY IMPORTANT)
        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        // ✅ 2. Allow auth endpoints
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String header = req.getHeader("Authorization");

        // ✅ 3. If no token → DON'T block (important for public APIs)
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // ✅ 4. If token invalid → DON'T block (let security handle)
        if (!jwtUtil.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ 5. Extract user info
        String email = jwtUtil.extractEmail(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.emptyList()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ 6. Continue request
        chain.doFilter(request, response);
    }
}
