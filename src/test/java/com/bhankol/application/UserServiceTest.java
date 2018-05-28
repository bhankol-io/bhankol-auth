package com.bhankol.application;

import com.bhankol.application.entity.User;
import com.bhankol.application.repository.UserRepository;
import com.bhankol.application.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by pravingosavi on 24/05/18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@WebAppConfiguration
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;



    @Test
    public void testGetAllUsers(){
        List<User> users = new ArrayList<User>();
        users.add(new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b"));
        users.add(new User("test2","test2","test2@gmail.com","firstname2","lastname2",123456,true,"5b02caf0199b3984491fb88b"));
        users.add(new User("test3","test3","test3@gmail.com","firstname3","lastname3",123456,true,"5b02caf0199b3984491fb88b"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(3, result.size());
    }

    @Test
    public void testGetByUserName(){
        User user =  new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");
        when(userRepository.findByUsername("test1")).thenReturn(user);
        User expectedUser = userService.getUserByUserName("test1");
        assertEquals("test1", expectedUser.getUsername());
        assertEquals("test1@gmail.com", expectedUser.getEmail());
        assertEquals("firstname1", expectedUser.getFirstname());
        assertEquals("lastname1", expectedUser.getLastname());
        assertEquals(Integer.valueOf(123456), expectedUser.getPhone());
        assertEquals(true, expectedUser.isActive());
        assertEquals("5b02caf0199b3984491fb88b", expectedUser.getClientid());
    }

    @Test
    public void testSaveUser(){
        User user =  new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");
        when(userRepository.save(user)).thenReturn(user);
        User savedUser=userService.creatNeweUser(user);
        assertEquals("test1", savedUser.getUsername());
        assertEquals("test1", savedUser.getPassword());
        assertEquals("test1@gmail.com", savedUser.getEmail());
        assertEquals("firstname1", savedUser.getFirstname());
        assertEquals("lastname1", savedUser.getLastname());
        assertEquals(Integer.valueOf(123456), savedUser.getPhone());
        assertEquals(true, savedUser.isActive());
        assertEquals("5b02caf0199b3984491fb88b", savedUser.getClientid());
    }


}
