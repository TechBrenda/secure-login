package org.techbrenda.securelogin.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
public class JwtProperties {
  
  private String signingSecret;
  private Long expirationMilliseconds;

  public String getSigningSecret() {
    return signingSecret;
  }

  public void setSigningSecret(String signingSecret) {
    this.signingSecret = signingSecret;
  }

  public Long getExpirationMilliseconds() {
    return expirationMilliseconds;
  }

  public void setExpirationMilliseconds(Long expirationMilliseconds) {
    this.expirationMilliseconds = expirationMilliseconds;
  }
}