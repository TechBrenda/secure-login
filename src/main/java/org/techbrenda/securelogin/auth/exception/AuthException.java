package org.techbrenda.securelogin.auth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthException extends RuntimeException {
  
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final long serialVersionUID = 2L;
  
  public AuthException(String message) {
    super(message);
    
    logger.debug(message);
  }
  
  public AuthException(String message, Throwable cause) {
    super(message, cause);
    
    logger.debug(message, cause);
  }
}