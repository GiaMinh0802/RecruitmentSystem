package com.fpt.recruitmentsystem.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateNewRefreshToken(Date timeExiration, UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    Date extractExiration(String token);
}
