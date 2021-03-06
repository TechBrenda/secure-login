package org.techbrenda.securelogin.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }
  
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf().disable();
    
    httpSecurity
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class);
        
    httpSecurity
        .authorizeRequests().antMatchers("/").permitAll()
        .and()
        .authorizeRequests().antMatchers("/h2-console/**").permitAll()
        .and()
        .authorizeRequests().antMatchers("/authenticate", "/register", "/confirmEmail").permitAll()
        .and()
        .authorizeRequests().anyRequest().authenticated();
    
    httpSecurity
        .headers().frameOptions().sameOrigin();
  }
}