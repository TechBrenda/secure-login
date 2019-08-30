package org.techbrenda.securelogin.auth.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.techbrenda.securelogin.auth.exception.AuthException;
import org.techbrenda.securelogin.auth.service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  private final static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      authorize(request);
    }
    
    filterChain.doFilter(request, response);
  }
  
  private void authorize(HttpServletRequest request) {
    String requestAuthHeader = request.getHeader("Authorization");
    String username = null;
    
    if (requestAuthHeader != null && requestAuthHeader.startsWith("Bearer ")) {
      // authentication depends on valid token
      String token = requestAuthHeader.substring(7);
      try {
        username = jwtService.getJwtSubject(token);
      } catch (AuthException e) {
        log.error(e.getMessage(), e);
      }
    }
    
    if (username != null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
      
      // User shall not be authenticated without verified authorization
      if (userDetails.isEnabled() 
            && userDetails.isAccountNonExpired() 
            && userDetails.isAccountNonLocked() 
            && userDetails.isCredentialsNonExpired()) {
        UsernamePasswordAuthenticationToken authenticationToken = new 
            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
  }
  
}