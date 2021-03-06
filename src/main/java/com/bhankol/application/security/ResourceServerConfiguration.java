package com.bhankol.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by pravingosavi on 21/05/18.
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter 
{
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .and().logout().logoutUrl("/logout")
            .logoutSuccessHandler(customLogoutSuccessHandler)
            .and()
            .csrf().disable()
            .headers()
            .frameOptions().disable()
            .and()
            .authorizeRequests()
                .antMatchers("/secure/**").authenticated()
            .antMatchers("/api/**").authenticated();
    }
}