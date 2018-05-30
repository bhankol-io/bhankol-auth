package com.bhankol.application;

/**
 * Created by pravingosavi on 24/05/18.
 */

import com.bhankol.application.controller.UserController;
import com.bhankol.application.entity.User;
import com.bhankol.application.security.RESTCorsFilter;
import com.bhankol.application.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("dev")
public class UserControllerUnitTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private static final int UNKNOWN_ID = Integer.MAX_VALUE;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .addFilters(new RESTCorsFilter())
                .build();
    }

    // =========================================== Get All Users ==========================================

    User user =  new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");

    @Test
    public void test_get_all_success() throws Exception {
        List<User> users = Arrays.asList(
                new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b"),
                new User("test2","test2","test2@gmail.com","firstname2","lastname2",123456,true,"5b02caf0199b3984491fb88b"));

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/user").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("test1")))
                .andExpect(jsonPath("$[0].password", is("test1")))
                .andExpect(jsonPath("$[0].email", is("test1@gmail.com")))
                .andExpect(jsonPath("$[0].firstname", is("firstname1")))
                .andExpect(jsonPath("$[0].lastname", is("lastname1")))
                .andExpect(jsonPath("$[0].phone", is(123456)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].clientid", is("5b02caf0199b3984491fb88b")))
                .andExpect(jsonPath("$[1].username", is("test2")))
                .andExpect(jsonPath("$[1].password", is("test2")))
                .andExpect(jsonPath("$[1].email", is("test2@gmail.com")))
                .andExpect(jsonPath("$[1].firstname", is("firstname2")))
                .andExpect(jsonPath("$[1].lastname", is("lastname2")))
                .andExpect(jsonPath("$[1].phone", is(123456)))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].clientid", is("5b02caf0199b3984491fb88b")));

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

     //=========================================== Get User By ID =========================================

    @Test
    public void test_get_by_username_success() throws Exception {
        User user = new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");

        when(userService.getUserByUserName("test1")).thenReturn(user);

        mockMvc.perform(get("/api/user/{username}", "test1").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.username", is("test1")))
                .andExpect(jsonPath("$.password", is("test1")))
                .andExpect(jsonPath("$.email", is("test1@gmail.com")))
                .andExpect(jsonPath("$.firstname", is("firstname1")))
                .andExpect(jsonPath("$.lastname", is("lastname1")))
                .andExpect(jsonPath("$.phone", is(123456)))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.clientid", is("5b02caf0199b3984491fb88b")));

        verify(userService, times(1)).getUserByUserName("test1");
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_get_by_username_fail_404_not_found() throws Exception {

        when(userService.getUserByUserName("abc")).thenReturn(null);

        mockMvc.perform(get("/api/user/{username}", "abc"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUserName("abc");
        verifyNoMoreInteractions(userService);
    }

    // =========================================== Create New User ========================================

    @Test
    public void test_create_user_success() throws Exception {
        User user = new User("test3","test3","test3@gmail.com","firstname3","lastname3",123456,true,"5b02caf0199b3984491fb88b");

        when(userService.exists(user)).thenReturn(false);
        doNothing().when(userService).createUser(user);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(user);
        verifyNoMoreInteractions(userService);
    }


    // =========================================== Update Existing User ===================================

    @Test
    public void test_update_user_success() throws Exception {
        User user = new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");

        when(userService.getUserByUserName(user.getUsername())).thenReturn(user);

        mockMvc.perform(
                put("/api/user/{username}", user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserByUserName(user.getUsername());
        verify(userService, times(1)).updateUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_update_user_fail_404_not_found() throws Exception {
        User user = new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");

        when(userService.getUserByUserName(user.getUsername())).thenReturn(null);

        mockMvc.perform(
                put("/api/user/{username}", user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUserName(user.getUsername());
        verifyNoMoreInteractions(userService);
    }

     //=========================================== Delete User ============================================

    @Test
    public void test_delete_user_success() throws Exception {
        User user = new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");

        when(userService.getUserByUserName(user.getUsername())).thenReturn(user);

        mockMvc.perform(
                delete("/api/user/{userid}", user.getUsername()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).getUserByUserName(user.getUsername());
        verify(userService, times(1)).deleteUserByUserName(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_delete_user_fail_404_not_found() throws Exception {
        User user = new User("test1","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");

        when(userService.getUserByUserName(user.getUsername())).thenReturn(null);

        mockMvc.perform(
                delete("/api/user/{userid}", user.getUsername()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUserName(user.getUsername());
        verifyNoMoreInteractions(userService);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


