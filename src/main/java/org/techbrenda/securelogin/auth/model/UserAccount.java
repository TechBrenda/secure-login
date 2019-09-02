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
  
  private Boolean enabled;
  
  // false if account locked for 13 months
  private Boolean accountNonExpired;
  
  private Boolean credentialsNonExpired;
  
  private Boolean accountNonLocked;
  
  private String passwordReminderToken;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss.SSSZ")
  private Date passwordReminderExpire;
  
  private String emailConfirmationToken;
  
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

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(Boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  public Boolean getCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public Boolean getAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(Boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
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

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }
  
  public void addAuthority(Authority authority) {
    if (!this.authorities.contains(authority)) {
      this.authorities.add(authority);
    }
  }
  
  public void removeAuthority(Authority authority) {
    if (this.authorities.contains(authority)) {
      this.authorities.remove(authority);
    }
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