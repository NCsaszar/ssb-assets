package com.smoothstack.branchservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//    MvcRequestMatcher apiMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/api/v1/**");
    MvcRequestMatcher swaggerMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/swagger-ui/**");
    MvcRequestMatcher apiDocsMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/v3/api-docs/**");
    MvcRequestMatcher swaggerHtmlMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/swagger-ui" +
            ".html");
    MvcRequestMatcher swaggerResourcesMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/swagger" +
            "-resources/**");
    MvcRequestMatcher webjarsMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/webjars/**");
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                    .requestMatchers(
//                            apiMatcher,
                            swaggerMatcher,
                            apiDocsMatcher,
                            swaggerHtmlMatcher,
                            swaggerResourcesMatcher,
                            webjarsMatcher
                    )
                    .permitAll().anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}

