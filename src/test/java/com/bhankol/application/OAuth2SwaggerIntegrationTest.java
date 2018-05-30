package com.bhankol.application;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

/**
 * Created by pravingosavi on 23/05/18.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@WebAppConfiguration
public class OAuth2SwaggerIntegrationTest {

    private String tokenValue = null;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Before
    public void obtainAccessToken() {
        final Map<String, String> params = new HashMap<String, String>();
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
                .post("/oauth/token");

        tokenValue = response.jsonPath()
                .getString("access_token");
    }

    @Test
    public void whenVerifySwaggerDocIsWorking_thenOK() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + tokenValue)
                .get("/v2/api-docs");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenVerifySwaggerUIIsWorking_thenOK() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + tokenValue)
                .get("/swagger-ui.html");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

}
