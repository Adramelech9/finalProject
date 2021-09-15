package com.itransition.training.task04.Users.repository;

import com.itransition.training.task04.Users.models.TableUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<TableUsers, String> {


    @Query("select count(t) from TableUsers t where t.socialNetwork=:socialNetwork")
    Long countBySocialNetwork(String socialNetwork);

}
