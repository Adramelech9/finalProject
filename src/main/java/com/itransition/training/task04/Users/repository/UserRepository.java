package com.itransition.training.task04.Users.repository;

import com.itransition.training.task04.Users.models.TableUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TableUsers, Long> {
    TableUsers findByUserName(String userName);
}
