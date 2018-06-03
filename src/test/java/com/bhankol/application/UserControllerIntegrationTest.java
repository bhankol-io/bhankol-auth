package com.bhankol.application;


import com.bhankol.application.entity.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.web.JsonPath;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by pravingosavi on 24/05/18.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
//@ActiveProfiles("dev")
public class UserControllerIntegrationTest {


    //private TestRestTemplate template=new TestRestTemplate();

    @Autowired
    private TestRestTemplate template;

    private static final String BASE_URI = "http://localhost:8080";
    private String tokenValue = null;

    User user =  new User("test12345","test1","test1@gmail.com","firstname1","lastname1",123456,true,"5b02caf0199b3984491fb88b");


    @Before
    public void obtainAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;");
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Basic Y2xpZW50aWQ6c2VjcmV0");
        System.out.println("+++++++++++++++++++++++++++"+"HELLO1");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = template.exchange("/oauth/token?username=testuser&password=test&grant_type=password", HttpMethod.POST,entity, String.class);
        System.out.println("+++++++++++++++++++++++++++"+"HELLO2");
        tokenValue = response.getBody().substring(17,693);
        System.out.println("+++++++++++++++++++++++++++"+tokenValue);
        System.out.println("+++++++++++++++++++++++++++"+response.getBody());
    }

// =========================================== Create New User ========================================

    @Test
    public void test_create_new_user_success(){
        System.out.println("+++++++++++++++++++++++++++"+tokenValue);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+tokenValue);
        headers.set("Accept", "application/json;");
        headers.set("Content-Type", "application/json");
        HttpEntity<User> entity = new HttpEntity<User>(user,headers);
        ResponseEntity<User> response = template.exchange("/api/user", HttpMethod.POST,entity, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    // =========================================== Get All Users ==========================================

//    @Test
//    public void test_get_all_success(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+tokenValue);
//        headers.set("Accept", "application/json;");
//        headers.set("Content-Type", "application/json");
//        HttpEntity<User> entity = new HttpEntity<User>(headers);
//        ResponseEntity<User[]> response = template.exchange(BASE_URI+"/api/user",HttpMethod.GET,entity,User[].class);
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//    }
//
////     =========================================== Get User By ID =========================================
//
//    @Test
//    public void test_get_by_id_success(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+tokenValue);
//        headers.set("Accept", "application/json;");
//        headers.set("Content-Type", "application/json");
//        HttpEntity<User> entity = new HttpEntity<User>(headers);
//        ResponseEntity<User> response = template.exchange(BASE_URI + "/api/user/testuser", HttpMethod.GET,entity,User.class);
//        User user = response.getBody();
//        assertThat(user.getUsername(), is("testuser"));
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//    }
//
//    @Test
//    public void test_get_by_id_failure_not_found(){
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+tokenValue);
//            headers.set("Accept", "application/json;");
//            headers.set("Content-Type", "application/json");
//            HttpEntity<User> entity = new HttpEntity<User>(headers);
//            ResponseEntity<User> response = template.exchange(BASE_URI + "/api/user/abc",HttpMethod.GET,entity, User.class);
//        } catch (HttpClientErrorException e){
//            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
//        }
//    }
//
//
//
//
////     =========================================== Update Existing User ===================================
//
//    @Test
//    public void test_update_user_success() {
//        User existingUser = new User("testuser", "test", "test1@gmail.com", "firstname123", "lastname1", 123456, true, "5b02caf0199b3984491fb88b");
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
//        headers.set("Accept", "application/json;");
//        headers.set("Content-Type", "application/json");
//        HttpEntity<User> entity = new HttpEntity<User>(existingUser, headers);
//        ResponseEntity<User> response = template.exchange(BASE_URI + "/api/user/testuser", HttpMethod.PUT, entity, User.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//    }
//
//    @Test
//    public void test_update_user_fail(){
//        User existingUser = new User("test2", "test1123", "test1@gmail.com", "firstname123", "lastname1", 123456, true, "5b02caf0199b3984491fb88b");
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
//            headers.set("Accept", "application/json;");
//            headers.set("Content-Type", "application/json");
//            HttpEntity<User> entity = new HttpEntity<User>(existingUser, headers);
//            ResponseEntity<User> response = template.exchange(BASE_URI + "/api/user/test2", HttpMethod.PUT, entity, User.class);
//        } catch (HttpClientErrorException e){
//            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
//        }
//    }
//
//    // =========================================== Delete User ============================================
//
//    @Test
//    public void test_delete_user_success(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
//        headers.set("Accept", "application/json;");
//        headers.set("Content-Type", "application/json");
//        HttpEntity<User> entity = new HttpEntity<User>(user, headers);
//        ResponseEntity<User> response = template.exchange(BASE_URI + "/api/user/test1", HttpMethod.DELETE, entity, User.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
//    }
//
//    @Test
//    public void test_delete_user_fail(){
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
//            headers.set("Accept", "application/json;");
//            headers.set("Content-Type", "application/json");
//            HttpEntity<User> entity = new HttpEntity<User>(user, headers);
//            ResponseEntity<User> response = template.exchange(BASE_URI + "/api/user/abc", HttpMethod.DELETE, entity, User.class);
//        } catch (HttpClientErrorException e){
//            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
//        }
//    }








}