package org.techbrenda.securelogin.auth.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "user_profile_seq", initialValue = 1, allocationSize = 1)
public class UserProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_seq")
  private Long id;
  
  @Column(nullable = false, unique = true, length = 254)
  private String email;
  
  @Column(nullable = false)
  private String firstName;
  
  @Column(nullable = false)
  private String lastName;
  
  private String displayName;
  
  private Boolean acceptTermsOfService;
  
  @Column(nullable = false)
  private String timeZone;
  
  @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
  private UserAccount userAccount;

  public UserProfile() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Boolean getAcceptTermsOfService() {
    return acceptTermsOfService;
  }

  public void setAcceptTermsOfService(Boolean acceptTermsOfService) {
    this.acceptTermsOfService = acceptTermsOfService;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  public UserAccount getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(UserAccount userAccount) {
    this.userAccount = userAccount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserProfile other = (UserProfile) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}