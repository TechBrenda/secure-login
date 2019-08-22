package org.techbrenda.securelogin.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  
  @PostMapping("/authenticate")
  public void createAuthToken() {}
  
  @PostMapping("/refresh")
  public void refreshAuthToken() {}
  
  @PostMapping("/register")
  public void registration() {}
}