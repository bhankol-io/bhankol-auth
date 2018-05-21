package com.bhankol.application.repository;

import com.bhankol.application.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by pravingosavi on 21/05/18.
 */
public interface UserRepository extends MongoRepository<User,String> {

    @Query("{'username':?0}")
    public User findByUsername(@Param("username") String username);
}
