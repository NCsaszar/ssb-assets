package com.smoothstack.userservice.config;

import com.smoothstack.userservice.model.AppUser;
import com.smoothstack.userservice.service.AppUserService;
import com.smoothstack.userservice.service.CustomOAuth2UserService;
import com.smoothstack.userservice.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtService jwtService;
    private final AppUserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(this::handleOauth2Success)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void handleOauth2Success(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        AppUser existingUser = userService.findByEmailOrUsername(email);

        if (existingUser != null) {
            String jwtToken = jwtService.generateToken(existingUser, existingUser.getRole().name(), existingUser.getUserId());
            Cookie jwtCookie = new Cookie("token", jwtToken);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 1 day expiration
            response.addCookie(jwtCookie);
            response.sendRedirect("http://localhost:5173/oauth2/redirect");
        } else {
            response.sendRedirect("http://localhost:5173/error");
        }
    }
}
