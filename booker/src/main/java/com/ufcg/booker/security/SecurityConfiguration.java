package com.ufcg.booker.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;


import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final BookerUserDetailsService userDetailsService;
    private final TokenManager tokenManager;
    private final BookerAccessDeniedHandler bookerAccessDeniedHandler;

    public SecurityConfiguration(BookerUserDetailsService userDetailsService, TokenManager tokenManager, BookerAccessDeniedHandler bookerAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
        this.bookerAccessDeniedHandler = bookerAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((auth) -> {
//                auth.requestMatchers("/signin", "/login").permitAll()
//                        .requestMatchers(PathRequest.toH2Console()).permitAll()
//                    .anyRequest().authenticated();
//            })
//            .cors().and()
//            .csrf().disable()
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .addFilterBefore(new JwtAuthenticationFilter(tokenManager, userDetailsService), UsernamePasswordAuthenticationFilter.class)
//            .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//            .accessDeniedHandler(bookerAccessDeniedHandler).and()
//            .headers().frameOptions().disable();
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/signin", "/login").permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .anyRequest().authenticated();
                })
                .cors() // Enable CORS
                .configurationSource(corsConfigurationSource()) // Provide a custom CorsConfigurationSource
                .and()
                .csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(tokenManager, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(bookerAccessDeniedHandler).and()
                .headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Set the allowed origins here
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Set the allowed HTTP methods here
        configuration.setAllowedHeaders(Arrays.asList("*")); // Set the allowed headers here
        configuration.setAllowCredentials(true); // Allow sending credentials (e.g., cookies) with the requests

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private static class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            String authorization = Objects.requireNonNullElse(request.getHeader("Authorization"), "[EMPTY]");
            logger.error("Access not verified was checked. Authorization: {}, Message: {}", authorization, authException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print("{\"error\": \"You are not authorized to use this resource\"}");
        }

    }
}
