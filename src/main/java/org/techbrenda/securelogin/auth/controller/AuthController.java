package org.techbrenda.securelogin.auth.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @PostMapping("/authenticate")
  public void createAuthToken(@RequestBody AuthRequest authRequest) {
    authenticate(authRequest.getEmail(), authRequest.getPassword());
  }
  
  @GetMapping("/refresh")
  public void refreshAuthToken() {}
  
  @PostMapping("/register")
  public void registration() {}
  
  private void authenticate(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new RuntimeException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new RuntimeException("INVALID_CREDENTIALS", e);
    } catch (LockedException e) {
      throw new RuntimeException("ACCOUNT_LOCKED", e);
    } catch (CredentialsExpiredException e) {
      throw new RuntimeException("PASSWORD_EXPIRED", e);
    } catch (AccountExpiredException e) {
      throw new RuntimeException("ACCOUNT_EXPIRED", e);
    }
  }
}