package org.techbrenda.securelogin.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.techbrenda.securelogin.auth.model.EmailVerification;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
  EmailVerification findByToken(String token);
}