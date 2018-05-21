package com.bhankol.application.repository;

import com.bhankol.application.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by pravingosavi on 21/05/18.
 */
public interface RoleRepository extends MongoRepository<Role,String> {

    @Query("{'username':?0}")
    Role findRolesByUsername(@Param("username") String username);
}
