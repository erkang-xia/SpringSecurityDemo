package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.model.UserDetailsPrinciple;
import com.example.springsecuritydemo.model.Users;
import com.example.springsecuritydemo.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepo repo;

    public UserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);
        if(user == null) {
            System.out.println("user not found");
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsPrinciple(user);
    }
}


