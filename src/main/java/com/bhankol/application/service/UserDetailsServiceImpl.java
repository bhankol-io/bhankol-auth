package com.bhankol.application.service;

import com.bhankol.application.entity.Role;
import com.bhankol.application.entity.User;
import com.bhankol.application.repository.RoleRepository;
import com.bhankol.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by pravingosavi on 21/05/18.
 */

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) {

        String lowercaseLogin = login.toLowerCase();

        User userFromDatabase;

            userFromDatabase = userRepository.findByUsername(lowercaseLogin);


        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } else if (!userFromDatabase.isActive()) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " is not activated");
        }

        Role roles = roleRepository.findRolesByUsername(userFromDatabase.getUsername());


        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : roles.getRolename().split(",")) {
            System.out.println("========================================"+authority);
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
            grantedAuthorities.add(grantedAuthority);
        }
        return new org.springframework.security.core.userdetails.User(userFromDatabase.getUsername(), userFromDatabase.getPassword(), grantedAuthorities);
    }
}
