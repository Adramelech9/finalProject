package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
