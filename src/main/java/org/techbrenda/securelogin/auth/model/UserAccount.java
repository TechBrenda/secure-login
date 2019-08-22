package org.techbrenda.securelogin.auth.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserAccount {
  
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(nullable = false, updatable = false)
  private UUID id;
  
  @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
  private UserProfile userProfile;
  
  @Column(nullable = false, unique = true, length = 254)
  private String email;
  
  @Column(nullable = false)
  private String password;
  
  @Column(nullable = false)
  private String passwordHashAlgorithm;
  
  private String passwordReminderToken;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss.SSSZ")
  private Date passwordReminderExpire;
  
  private String emailConfirmationToken;
  
  @ManyToOne
  private UserAccountStatus userAccountStatus;
  
  @ManyToMany
  private Set<Authority> authorities = new HashSet<>();

  public UserAccount() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordHashAlgorithm() {
    return passwordHashAlgorithm;
  }

  public void setPasswordHashAlgorithm(String passwordHashAlgorithm) {
    this.passwordHashAlgorithm = passwordHashAlgorithm;
  }

  public String getPasswordReminderToken() {
    return passwordReminderToken;
  }

  public void setPasswordReminderToken(String passwordReminderToken) {
    this.passwordReminderToken = passwordReminderToken;
  }

  public Date getPasswordReminderExpire() {
    return passwordReminderExpire;
  }

  public void setPasswordReminderExpire(Date passwordReminderExpire) {
    this.passwordReminderExpire = passwordReminderExpire;
  }

  public String getEmailConfirmationToken() {
    return emailConfirmationToken;
  }

  public void setEmailConfirmationToken(String emailConfirmationToken) {
    this.emailConfirmationToken = emailConfirmationToken;
  }

  public UserAccountStatus getUserAccountStatus() {
    return userAccountStatus;
  }

  public void setUserAccountStatus(UserAccountStatus userAccountStatus) {
    this.userAccountStatus = userAccountStatus;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
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
    UserAccount other = (UserAccount) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}