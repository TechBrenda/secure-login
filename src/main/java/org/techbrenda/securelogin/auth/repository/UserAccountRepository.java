package org.techbrenda.securelogin.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.techbrenda.securelogin.auth.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
  
  UserAccount findByEmail(String email);
}