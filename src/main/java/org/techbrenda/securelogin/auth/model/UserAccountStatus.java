package org.techbrenda.securelogin.auth.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
// TODO make fields private and add boilerplate
@Entity
@Table(schema = "Admin")
@SequenceGenerator(name = "user_status_seq", initialValue = 1, allocationSize = 1)
public class UserAccountStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_status_seq")
  Long id;
  
  @Column(nullable = false)
  String code;
  
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_account_status_id", referencedColumnName = "id")
  UserAccount userAccount;
}