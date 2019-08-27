package org.techbrenda.securelogin.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.techbrenda.securelogin.auth.service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    
    authorize(request);
    
    filterChain.doFilter(request, response);
  }
  
  private void authorize(HttpServletRequest request) {
    String requestAuthHeader = request.getHeader("Authorization");
    String username = null;
    
    if (requestAuthHeader != null && requestAuthHeader.startsWith("Bearer ")) {
      username = jwtService.getJwtSubject(requestAuthHeader.substring(7));
    }
    
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
      
      // JWT is only for authentication. User access must be verified.
      if (userDetails.isEnabled() 
            && userDetails.isAccountNonExpired() 
            && userDetails.isAccountNonLocked() 
            && userDetails.isCredentialsNonExpired()) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
  }
  
}