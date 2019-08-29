package org.techbrenda.securelogin.auth.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.techbrenda.securelogin.auth.service.JwtService;

@RestController
public class AuthController {
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtService jwtService;
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest authRequest) throws AuthException {
    authenticate(authRequest.getEmail(), authRequest.getPassword());
    
    UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
    AuthResponse authResponse = new AuthResponse(jwtService.getJwtSubject(userDetails.getUsername()));
      
    return ResponseEntity.ok(authResponse);
  }
  
  @GetMapping("/refresh")
  public void refreshAuthToken(HttpServletRequest request) {
    
  }
  
  @PostMapping("/register")
  public void registration() {}
  
  @ExceptionHandler({ AuthException.class })
  public ResponseEntity<String> handleAuthException(AuthException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }
  
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