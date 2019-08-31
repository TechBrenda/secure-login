package org.techbrenda.securelogin.auth.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@SequenceGenerator(name = "email_verification_seq", initialValue = 1, allocationSize = 1)
public class EmailVerification {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_seq")
  private Long id;
  
  @Column(nullable = false)
  private UUID token;
  
  @OneToOne(fetch = FetchType.EAGER)
  private UserAccount userAccount;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss.SSSZ")
  private Date expiration;

  public EmailVerification() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getToken() {
    return token;
  }

  public void setToken(UUID token) {
    this.token = token;
  }

  public UserAccount getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(UserAccount userAccount) {
    this.userAccount = userAccount;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    result = prime * result + ((userAccount == null) ? 0 : userAccount.hashCode());
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
    EmailVerification other = (EmailVerification) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    if (userAccount == null) {
      if (other.userAccount != null)
        return false;
    } else if (!userAccount.equals(other.userAccount))
      return false;
    return true;
  }
}