package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.model.Users;
import com.example.springsecuritydemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Users registerUser(Users user);
    String verify(Users user);

}
