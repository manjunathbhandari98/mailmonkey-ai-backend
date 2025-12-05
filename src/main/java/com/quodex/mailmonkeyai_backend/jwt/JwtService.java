package com.quodex.mailmonkeyai_backend.jwt;

import com.quodex.mailmonkeyai_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-expiration-ms}")
  private long accessTokenMs;

  @Value("${jwt.refresh-token-expiration-ms}")
  private long refreshTokenMs;

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String buildToken(Map<String, Object> claims, String subject, long expiration) {
    return Jwts.builder()
      .claims(claims)
      .subject(subject)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSignInKey())
      .compact();
  }

  public String generateAccessToken(User user) {
    return buildToken(Map.of("role", user.getRole()), user.getEmail(), accessTokenMs);
  }

  public String generateRefreshToken(User user) {
    return buildToken(Map.of("type", "refresh"), user.getEmail(), refreshTokenMs);
  }

  public boolean isTokenValid(String token, User user) {
    return extractEmail(token).equals(user.getEmail()) && !isExpired(token);
  }

  public String extractEmail(String token) {
    return extractClaims(token).getSubject();
  }

  private boolean isExpired(String token) {
    return extractClaims(token).getExpiration().before(new Date());
  }

  private Claims extractClaims(String token) {
    return Jwts.parser()
      .verifyWith(getSignInKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
