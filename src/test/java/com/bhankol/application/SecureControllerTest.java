package com.bhankol.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bhankol.application.controller.SecureController;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

/**
 * Created by pravingosavi on 24/05/18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("dev")
public class SecureControllerTest {

    private static final String URL_PREFIX ="http://localhost:8080";
    private String tokenValue = null;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @InjectMocks
    SecureController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void greetingUnauthorized() throws Exception {
        mvc.perform(get("/secure")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void greetingAuthorized() throws Exception {
        System.out.println(tokenValue);
        mvc.perform(get("/secure")
                .header("Authorization", "Bearer " + tokenValue))
                .andExpect(status().isOk());
    }

    @Before
    public void obtainAccessToken() {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("scope", "read write");
        params.put("username", "testuser");
        params.put("password", "test");
        final Response response = RestAssured.given()
                .auth()
                .preemptive()
                .basic("clientid", "secret")
                .and()
                .with()
                .params(params)
                .when()
                .post(URL_PREFIX + "/oauth/token");

        tokenValue = response.jsonPath()
                .getString("access_token");
    }



}
