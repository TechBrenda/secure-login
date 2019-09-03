package org.techbrenda.securelogin.auth.event;

import org.springframework.context.ApplicationEvent;
import org.techbrenda.securelogin.auth.model.UserAccount;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;
  
  private UserAccount userAccount;
  
  public OnRegistrationCompleteEvent(UserAccount userAccount) {
    super(userAccount);
    
    this.userAccount = userAccount;
  }

  public UserAccount getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(UserAccount userAccount) {
    this.userAccount = userAccount;
  }
}