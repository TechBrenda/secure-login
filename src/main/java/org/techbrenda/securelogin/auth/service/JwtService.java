package org.techbrenda.securelogin.auth.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techbrenda.securelogin.auth.config.JwtProperties;
import org.techbrenda.securelogin.auth.exception.AuthException;

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
    } catch (ExpiredJwtException e) {
      throw new AuthException("JWT_EXPIRED", e);
    } catch (UnsupportedJwtException e) {
      throw new AuthException("JWT_UNSUPPORTED", e);
    } catch (MalformedJwtException e) {
      throw new AuthException("JWT_MALFORMED", e);
    } catch (SignatureException e) {
      throw new AuthException("JWT_SIGNATURE_EXCEPTION", e);
    } catch (IllegalArgumentException e) {
      throw new AuthException("JWT_MISSING", e);
    }
  }
  
  public String getJwtSubject(String jwt) throws AuthException {
    Claims claims = parseJWT(jwt);
    return claims.getSubject();
  }
  
  public String refreshJWT(String jwt) throws AuthException {
    return createJWT(getJwtSubject(jwt));
  }
}