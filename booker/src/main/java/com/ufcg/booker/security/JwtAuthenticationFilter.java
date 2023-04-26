package com.ufcg.booker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final BookerUserDetailsService usersService;

    public JwtAuthenticationFilter(TokenManager tokenManager, BookerUserDetailsService usersService) {
        this.tokenManager = tokenManager;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> possibleToken = getTokenFromRequest(request);
        if (possibleToken.isPresent() && tokenManager.isValid(possibleToken.get())) {
            String username = tokenManager.getUsername(possibleToken.get());
            UserDetails userDetails = usersService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization"); //TODO Accept as Bearer token
        return Optional.ofNullable(authToken);
    }
}
