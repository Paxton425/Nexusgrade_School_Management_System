package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserByInstructor_Id(UUID instructorId);
}
