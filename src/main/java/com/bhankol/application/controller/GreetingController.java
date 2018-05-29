package com.bhankol.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pravingosavi on 29/05/18.
 */
public class GreetingController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String sayHello() {
        return "Secure World!";
    }
}
