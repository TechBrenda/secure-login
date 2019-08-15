package org.techbrenda.securelogin.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
// TODO make fields private and add boilerplate
@Entity
@Table(schema = "Admin")
public class UserAccount {
  
  @Id
  Long id;
  
  @OneToOne
  @MapsId
  UserProfile userProfile;
  
  @Column(nullable = false)
  String password;
  
  @Column(nullable = false)
  String passwordHashAlgorithm;
  
  String passwordReminderToken;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss.SSSZ")
  Date passwordReminderExpire;
  
  String emailConfirmationToken;
  
  @OneToOne(mappedBy = "user_profile")
  UserAccountStatus userAccountStatus;
}