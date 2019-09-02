package org.techbrenda.securelogin.auth.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.techbrenda.securelogin.auth.dto.RegisterRequest;
import org.techbrenda.securelogin.auth.model.Authority;
import org.techbrenda.securelogin.auth.model.EmailVerification;
import org.techbrenda.securelogin.auth.model.UserAccount;
import org.techbrenda.securelogin.auth.model.UserProfile;
import org.techbrenda.securelogin.auth.repository.AuthorityRepository;
import org.techbrenda.securelogin.auth.repository.EmailVerificationRepository;
import org.techbrenda.securelogin.auth.repository.UserAccountRepository;
import org.techbrenda.securelogin.auth.repository.UserProfileRepository;

@Service
public class RegistrationService {
  
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  
  @Autowired
  private AuthorityRepository authorityRepo;
  
  @Autowired
  private UserAccountRepository userAccountRepo;
  
  @Autowired
  private UserProfileRepository userProfileRepo;
  
  @Autowired
  private EmailVerificationRepository emailVerificationRepo;
  
  public void registerNewUser(RegisterRequest registration) {
    UserAccount userAccount = new UserAccount();
    userAccount.setEmail(registration.getEmail());
    userAccount.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
    userAccount.setEnabled(false);
    userAccount.setAccountNonExpired(true);
    userAccount.setAccountNonLocked(true);
    userAccount.setCredentialsNonExpired(true);
    
    Authority userAuthority = authorityRepo.findByRolename("ROLE_USER");
    userAccount.addAuthority(userAuthority);
    userAccountRepo.save(userAccount);
    
    UserProfile userProfile = new UserProfile();
    userProfile.setFirstName(registration.getFirstname());
    userProfile.setLastName(registration.getLastname());
    userProfile.setTimeZone(registration.getTimezone());
    userProfile.setUserAccount(userAccount);
    userProfileRepo.save(userProfile);
    
    // createVerificationToken(userAccount);
  }
  
  private Date calculateExpiration() {
    int minutesInOneDay = 24 * 60;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(new Date().getTime());
    calendar.add(Calendar.MINUTE, minutesInOneDay);
    return new Date(calendar.getTime().getTime());
  }
  
  public void createVerificationToken(UserAccount userAccount) {
    String token = UUID.randomUUID().toString();
    Date expiration = calculateExpiration();
    EmailVerification verification = new EmailVerification(token, userAccount, expiration);
    emailVerificationRepo.save(verification);
  }
}