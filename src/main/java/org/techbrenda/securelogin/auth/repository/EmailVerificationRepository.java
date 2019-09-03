package org.techbrenda.securelogin.auth.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.techbrenda.securelogin.auth.model.EmailVerification;
import org.techbrenda.securelogin.auth.model.UserAccount;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
  EmailVerification findByToken(String token);
  
  List<EmailVerification> findByUserAccount(UserAccount userAccount);
}