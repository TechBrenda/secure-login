package org.techbrenda.securelogin.auth.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
// TODO make fields private and add boilerplate
@Entity
@Table(schema = "Admin")
@SequenceGenerator(name = "user_profile_seq", initialValue = 1, allocationSize = 1)
public class UserProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_seq")
  Long id;
  
  @Column(nullable = false, unique = true, length = 254)
  String email;
  
  @Column(nullable = false)
  String firstName;
  
  @Column(nullable = false)
  String lastName;
  
  String displayName;
  
  Boolean acceptTermsOfService;
  
  @Column(nullable = false)
  String timeZone;
  
  @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
  UserAccount userAccount;
}