package org.techbrenda.securelogin.auth.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techbrenda.securelogin.auth.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtService {
  
  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
  
  /* @Value("${jwt.signing-secret}")
  private String secret;
  
  @Value("${jwt.expiration-milliseconds}")
  private String expMilliseconds; */
  
  @Autowired
  private JwtProperties properties;
  
  public String createJWT(String subject) {
    if (subject == null) {
      return null;
    }
    
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    
    byte[] keySecretBytes = DatatypeConverter.parseBase64Binary(properties.getSigningSecret());
    Key signingKey = new SecretKeySpec(keySecretBytes, signatureAlgorithm.getJcaName());
    
    JwtBuilder builder = Jwts.builder()
                              .setIssuedAt(now)
                              .setSubject(subject)
                              .signWith(signatureAlgorithm, signingKey);
    
    long ttlMillis = properties.getExpirationMilliseconds();
    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date expDate = new Date(expMillis);
      builder.setExpiration(expDate);
    }
    
    return builder.compact();
  }
  
  private Claims parseJWT(String jwt) {
    try {
    Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(properties.getSigningSecret()))
                .parseClaimsJws(jwt)
                .getBody();
    return claims;
    } catch (ExpiredJwtException exception) {
      logger.warn("Request to parse expired JWT: {} failed: {}", jwt, exception.getMessage());
    } catch (UnsupportedJwtException exception) {
      logger.warn("Request to parse unsupported JWT: {} failed: {}", jwt, exception.getMessage());
    } catch (MalformedJwtException exception) {
      logger.warn("Request to parse invalid JWT: {} failed: {}", jwt, exception.getMessage());
    } catch (SignatureException exception) {
      logger.warn("Request to parse JWT with invalid signature: {} failed: {}", jwt, exception.getMessage());
    } catch (IllegalArgumentException exception) {
      logger.warn("Request to parse empty or null JWT: {} failed: {}", jwt, exception.getMessage());
    }
    
    return null;
  }
  
  public String getJwtSubject(String jwt) {
    Claims claims = parseJWT(jwt);
    if (claims == null) {
      return null;
    }
    return claims.getSubject();
  }
  
  public String refreshJWT(String jwt) {
    return createJWT(getJwtSubject(jwt));
  }
}