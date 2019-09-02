package org.techbrenda.securelogin.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.techbrenda.securelogin.auth.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
  Authority findByRolename(String role);
}