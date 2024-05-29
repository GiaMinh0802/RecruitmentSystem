package com.fpt.recruitmentsystem.configuration;

import com.fpt.recruitmentsystem.model.Token;
import com.fpt.recruitmentsystem.service.IJwtService;
import com.fpt.recruitmentsystem.repository.TokenRepository;
import com.fpt.recruitmentsystem.util.ResponseMessage;
import com.fpt.recruitmentsystem.util.Utility;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Headers", "*");
        final String authHeader = request.getHeader("Authorization");
        final String JWT;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        JWT = authHeader.substring(7);
        try {
            userEmail = jwtService.extractUsername(JWT);
        } catch (ExpiredJwtException e) {
            ResponseMessage responseMessage = new ResponseMessage("Token has expired");
            Utility.responseObject(response, responseMessage);
            return;
        }

        Optional<Token> tokenOptional = tokenRepository.findTokenByAccountEmail(userEmail);
        Token token = tokenOptional.isPresent() ? tokenOptional.get() : null;
        
        if (token == null || token.getToken() == null) {
            ResponseMessage responseMessage = new ResponseMessage("Account logged out");
            Utility.responseObject(response, responseMessage);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(JWT, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }


}
