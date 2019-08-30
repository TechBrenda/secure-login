package org.techbrenda.securelogin.auth.exception;

public class AuthException extends RuntimeException {

  private static final long serialVersionUID = 2L;
  
  public AuthException(String message) {
    super(message);
  }
  
  public AuthException(String message, Throwable cause) {
    super(message, cause);
  }
}