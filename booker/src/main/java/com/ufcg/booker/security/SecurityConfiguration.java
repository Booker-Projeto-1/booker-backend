package com.ufcg.booker.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final BookerUserDetailsService userDetailsService;
    private final TokenManager tokenManager;

    public SecurityConfiguration(BookerUserDetailsService userDetailsService, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> {
                auth.requestMatchers("/signin", "/login").permitAll()
                    .requestMatchers(toH2Console()).permitAll()
                    .anyRequest().authenticated();
            })
            .cors().and()
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new JwtAuthenticationFilter(tokenManager, userDetailsService), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
            .headers().frameOptions().disable();

        return http.build();
    }

    private static class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            logger.error("Access not verified was checked. Message: {}", authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to use this resource");
        }

    }
}
