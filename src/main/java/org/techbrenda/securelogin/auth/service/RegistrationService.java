package org.techbrenda.securelogin.auth.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
  
  public UserAccount registerNewUser(RegisterRequest registration) {
    UserAccount newUserAccount = userAccountRepo.findByEmail(registration.getEmail());
    if (newUserAccount != null) {
      // User account already exists. New user cannot be created
      return null;
    } else {
      newUserAccount = new UserAccount();
      newUserAccount.setEmail(registration.getEmail());
      newUserAccount.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
      newUserAccount.setEnabled(false);
      newUserAccount.setAccountNonExpired(true);
      newUserAccount.setAccountNonLocked(true);
      newUserAccount.setCredentialsNonExpired(true);
      
      Authority userAuthority = authorityRepo.findByRolename("ROLE_USER");
      newUserAccount.addAuthority(userAuthority);
      userAccountRepo.save(newUserAccount);
      
      UserProfile userProfile = new UserProfile();
      userProfile.setFirstName(registration.getFirstname());
      userProfile.setLastName(registration.getLastname());
      userProfile.setTimeZone(registration.getTimezone());
      userProfile.setUserAccount(newUserAccount);
      userProfileRepo.save(userProfile);
      
      return newUserAccount;
    }
  }
  
  private Date calculateExpiration() {
    int minutesInOneDay = 24 * 60;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(new Date().getTime());
    calendar.add(Calendar.MINUTE, minutesInOneDay);
    return new Date(calendar.getTime().getTime());
  }
  
  public String createVerificationToken(UserAccount userAccount) {
    String token = UUID.randomUUID().toString();
    Date expiration = calculateExpiration();
    EmailVerification verification = new EmailVerification(token, userAccount, expiration);
    emailVerificationRepo.save(verification);
    
    return verification.getToken();
  }
  
  public EmailVerification getEmailVerificationFromToken(String token) {
    return emailVerificationRepo.findByToken(token);
  }
  
  public boolean isTokenExpired(Date expiration) {
    Date rightNow = new Date();
    
    return expiration.before(rightNow);
  }
  
  public UserAccount getUserAccountFromEmailVerification(EmailVerification verification) {
    return userAccountRepo.findById(verification.getUserAccount().getId()).get();
  }
  
  public void enableUserAccount(UserAccount userAccount) {
    userAccount.setEnabled(true);
    userAccountRepo.save(userAccount);
    
    removeUsedEmailVerifications(userAccount);
  }
  
  private void removeUsedEmailVerifications(UserAccount userAccount) {
    List<EmailVerification> userTokens = emailVerificationRepo.findByUserAccount(userAccount);
    for (EmailVerification verification : userTokens) {
      emailVerificationRepo.delete(verification);
    }
  }
}