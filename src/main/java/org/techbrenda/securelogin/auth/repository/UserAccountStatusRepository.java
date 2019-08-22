package org.techbrenda.securelogin.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.techbrenda.securelogin.auth.model.UserAccountStatus;

@Repository
public interface UserAccountStatusRepository extends JpaRepository<UserAccountStatus, Long> {}