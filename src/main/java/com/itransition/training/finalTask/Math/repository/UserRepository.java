package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
