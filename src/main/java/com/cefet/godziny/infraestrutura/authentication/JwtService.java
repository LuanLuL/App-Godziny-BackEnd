package com.cefet.godziny.infraestrutura.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.cefet.godziny.infraestrutura.rest.auth.AuthRestConverter;

@Service
public class JwtService {

  private static final long JWT_EXPIRATION = 86400000;
  private static final long REFRESH_EXPIRATION  = 604800000;
  private static final String SECRET_KEY = "c41b5baaaee57ce9d68912da7d3fa9f94cbd66b76ae46d6ad915c0d9d272fac6";

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, REFRESH_EXPIRATION);
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails, JWT_EXPIRATION);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    return AuthRestConverter.getToken(extraClaims, userDetails, expiration, getSignInKey());
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()));
  }

  private Claims extractAllClaims(String token) {
    return AuthRestConverter.getAllClaims(token, getSignInKey());
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}