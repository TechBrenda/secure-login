package org.techbrenda.securelogin.auth.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
  
  @Value("${jwt.signing.key.secret}")
  private String secret;
  
  public String createJWT(String id, String issuer, String subject, long ttlMillis) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    
    byte[] keySecretBytes = DatatypeConverter.parseBase64Binary(secret);
    Key signingKey = new SecretKeySpec(keySecretBytes, signatureAlgorithm.getJcaName());
    
    JwtBuilder builder = Jwts.builder()
                              .setId(id)
                              .setIssuedAt(now)
                              .setSubject(subject)
                              .setIssuer(issuer)
                              .signWith(signatureAlgorithm, signingKey);
    
    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date expDate = new Date(expMillis);
      builder.setExpiration(expDate);
    }
    
    return builder.compact();
  }
  
  private Claims parseJWT(String jwt, String subject) {
    Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(jwt)
                .getBody();
                
    
                
    return claims;
  }
  
  public String refreshJWT(String jwt, String subject, long ttlMillis) {
    Claims claims = parseJWT(jwt, subject);
    
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    claims.setIssuedAt(now);
    
    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date expDate = new Date(expMillis);
      claims.setExpiration(expDate);
    }
    
    return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.parseBase64Binary(secret))
                .compact();
  }
}