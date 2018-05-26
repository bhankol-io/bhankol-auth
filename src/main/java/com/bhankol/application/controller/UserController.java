package com.bhankol.application.controller;

import com.bhankol.application.entity.User;
import com.bhankol.application.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by pravingosavi on 21/05/18.
 */

@RestController
@RequestMapping(value = "/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //-------------------Retrieve All Users--------------------------------------------------------

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ApiOperation(value = "View a list of User's",response = User.class, responseContainer="List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Users"),
            @ApiResponse(code = 204, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.getAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    //-------------------Retrieve Single User--------------------------------------------------------

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "View a User",response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved User"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        System.out.println("Fetching User with username " + username);
        User user = userService.getUserByUserName(username);
        if (user == null) {
            System.out.println("User with username " + username + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ApiOperation(value = "Create a User",response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created User")
    })
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("Creating User " + user.getUsername());
        userService.createUser(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a User",response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a User"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<User> updateUser(@PathVariable("username") String username, @RequestBody User user) {
        System.out.println("Updating User " + username);

        User currentUser = userService.getUserByUserName(username);

        if (currentUser==null) {
            System.out.println("User with username " + username + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        if(user.getClientid()!=null){
            currentUser.setClientid(user.getClientid());
        }
        if(user.getEmail()!=null){
            currentUser.setEmail(user.getEmail());
        }
        if(user.getFirstname()!=null){
            currentUser.setFirstname(user.getFirstname());
        }
        if(user.getLastname()!=null){
            currentUser.setLastname(user.getLastname());
        }
        if(user.getPassword()!=null){
            currentUser.setPassword(user.getPassword());
        }
        if(user.getPhone().toString()!=null){
            currentUser.setPhone(user.getPhone());
        }

        userService.updateUser(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "/user/{userid}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a User",response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted User"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })    public ResponseEntity<User> deleteUser(@PathVariable("userid") String userid) {
        System.out.println("Fetching & Deleting User with username " + userid);

        User user = userService.getUserByUserName(userid);
        if (user == null) {
            System.out.println("Unable to delete. User with username " + userid + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        userService.deleteUserByUserName(user);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }


    //------------------- Delete All Users --------------------------------------------------------

    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete All Users",response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted Users")
    })
    public ResponseEntity<User> deleteAllUsers() {
        System.out.println("Deleting All Users");

        userService.deleteAllUsers();
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}
