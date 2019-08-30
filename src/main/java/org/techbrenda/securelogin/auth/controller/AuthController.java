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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.techbrenda.securelogin.auth.exception.AuthException;
import org.techbrenda.securelogin.auth.service.JwtService;

@RestController
public class AuthController {
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtService jwtService;
  
  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest authRequest) throws AuthException {
    // throws AuthException if authentication fails
    Authentication authenticatedUser = authenticate(authRequest.getEmail(), authRequest.getPassword());
    
    UserDetails userDetails = (UserDetails) authenticatedUser.getPrincipal();
    AuthResponse authResponse = new AuthResponse(jwtService.createJWT(userDetails.getUsername()));
      
    return ResponseEntity.ok(authResponse);
  }
  
  @GetMapping("/refresh")
  public ResponseEntity<?> refreshAuthToken(HttpServletRequest request) throws AuthException {
    // authorization takes place in JwtAuthenticationFilter
    // missing Auth header routes to Http403ForbiddenEntryPoint and never reaches this method
    String token = request.getHeader("Authorization").substring(7);
    
    // throws AuthException if exception found during token parsing
    String refreshedToken = jwtService.refreshJWT(token);
    
    return ResponseEntity.ok(new AuthResponse(refreshedToken));
  }
  
  @PostMapping("/register")
  public void registration() {}
  
  @ExceptionHandler({ AuthException.class })
  public ResponseEntity<String> handleAuthException(AuthException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }
  
  private Authentication authenticate(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    
    try {
      return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new AuthException("USER_DISABLED", e);
    } catch (LockedException e) {
      throw new AuthException("ACCOUNT_LOCKED", e);
    } catch (CredentialsExpiredException e) {
      throw new AuthException("PASSWORD_EXPIRED", e);
    } catch (AccountExpiredException e) {
      throw new AuthException("ACCOUNT_EXPIRED", e);
    } catch (BadCredentialsException e) {
      throw new AuthException("INVALID_CREDENTIALS", e);
    }
  }
}