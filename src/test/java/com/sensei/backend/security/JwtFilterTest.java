package com.sensei.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author vaishnav88sk
 */
@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void testAuthEndpointsAreAllowedWithoutToken() throws ServletException, IOException {
        request.setRequestURI("/api/auth/google-login");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testActuatorEndpointsAreAllowedWithoutToken() throws ServletException, IOException {
        request.setRequestURI("/actuator/health");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testOptionsPreflightRequestsAreAllowedWithoutToken() throws ServletException, IOException {
        request.setRequestURI("/api/parent-user/me");
        request.setMethod("OPTIONS");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testMissingTokenReturnsUnauthorized() throws ServletException, IOException {
        request.setRequestURI("/api/some-protected-route");

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Missing or invalid Authorization header"));
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testInvalidTokenReturnsUnauthorized() throws ServletException, IOException {
        request.setRequestURI("/api/some-protected-route");
        request.addHeader("Authorization", "Bearer invalidToken");

        when(jwtUtil.validateToken("invalidToken")).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid or expired token"));
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testValidTokenSetsAuthentication() throws ServletException, IOException {
        request.setRequestURI("/api/some-protected-route");
        request.addHeader("Authorization", "Bearer validToken");

        when(jwtUtil.validateToken("validToken")).thenReturn(true);
        when(jwtUtil.extractEmail("validToken")).thenReturn("test@example.com");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("test@example.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
