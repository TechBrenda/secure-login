package org.techbrenda.securelogin.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.techbrenda.securelogin.auth.model.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {}