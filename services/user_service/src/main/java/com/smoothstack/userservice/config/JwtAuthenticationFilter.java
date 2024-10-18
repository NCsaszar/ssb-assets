package com.smoothstack.userservice.config;

import com.smoothstack.userservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String appUserEmail = null;

        // Extract JWT token from Authorization header if present
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            // If Authorization header is not present, try to extract JWT token from cookies
            if (request.getCookies() != null) {
                jwt = Arrays.stream(request.getCookies())
                        .filter(cookie -> "token".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
            }
        }

        // It first checks if the incoming request has an Authorization header with a bearer token.
        // If not, it allows the request to proceed without authentication.
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // If a token is present, the filter extracts the username from the JWT and checks if the user is already authenticated.
        appUserEmail = jwtService.extractAppUserName(jwt);
        if (appUserEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // If the user is not authenticated
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(appUserEmail);
            // it retrieves user details from the database
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // validates the token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        // and creates an UsernamePasswordAuthenticationToken if the token is valid.
                        userDetails,
                        null,
                        userDetails.getAuthorities() // retrieves the authorities (roles) associated with the user.
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // sets the UsernamePasswordAuthenticationToken in the security context
            }
        }
        filterChain.doFilter(request, response);
    }
}
