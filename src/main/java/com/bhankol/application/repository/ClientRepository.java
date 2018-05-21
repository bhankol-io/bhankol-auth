package com.bhankol.application.repository;

import com.bhankol.application.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Created by pravingosavi on 21/05/18.
 */
public interface ClientRepository extends MongoRepository<Client,String>{

    @Query("{'clientid':?0}")
    Client getById(@Param("clientid") String clientid);
}
