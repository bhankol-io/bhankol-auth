package com.bhankol.application.controller;

import com.bhankol.application.entity.User;
import com.bhankol.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pravingosavi on 21/05/18.
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("Creating User " + user.getUsername());
        userService.createUser(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
}
