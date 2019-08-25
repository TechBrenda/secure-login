package org.techbrenda.securelogin.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
		return null;
	}
  
}