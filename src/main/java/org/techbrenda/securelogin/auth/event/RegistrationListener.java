package org.techbrenda.securelogin.auth.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.techbrenda.securelogin.auth.model.UserAccount;
import org.techbrenda.securelogin.auth.service.RegistrationService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
  
  @Autowired
  private RegistrationService registrationService;
  
  @Autowired
  private JavaMailSender mailSender;

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    confirmRegistration(event);
  }
  
  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    UserAccount userAccount = event.getUserAccount();
    String token = registrationService.createVerificationToken(userAccount);
    String recipientAddress = userAccount.getEmail();
    String subject = "Registration Confirmation";
    String confirmationUrl = "http://localhost:8080/confirmEmail?token=" + token;
    String message = "Please click the following link to confirm your email:\n\n" + confirmationUrl;
    
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }
}