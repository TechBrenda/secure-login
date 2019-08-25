package org.techbrenda.securelogin.auth.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techbrenda.securelogin.auth.model.Authority;
import org.techbrenda.securelogin.auth.model.UserAccount;
import org.techbrenda.securelogin.auth.repository.UserAccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  UserAccountRepository userAccountRepo;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserAccount userAccount = userAccountRepo.findByEmail(email);
    if (userAccount == null) {
      throw new UsernameNotFoundException(email);
    }
    
    Set<GrantedAuthority> authorities = new HashSet<>();
    for (Authority authority : userAccount.getAuthorities()) {
      authorities.add(new SimpleGrantedAuthority(authority.getRole()));
    }
    
    return new User(userAccount.getEmail(),
                userAccount.getPassword(),
                userAccount.getEnabled(),
                userAccount.getAccountNonExpired(),
                userAccount.getCredentialsNonExpired(),
                userAccount.getAccountNonLocked(),
                authorities);
	}
  
}