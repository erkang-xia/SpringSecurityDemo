package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.model.Users;
import com.example.springsecuritydemo.repo.UserRepo;
import com.example.springsecuritydemo.service.JwtService;
import com.example.springsecuritydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Users registerUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public String verify(Users user) {
        //用户提交登录请求
        //使用 UsernamePasswordAuthenticationToken 将用户名和密码封装
        //调用 AuthenticationManager 进行验证
        //通过 AuthenticationManager 调用已注册的 AuthenticationProvider 进行身份验证。
        //调用AuthenticationManager.authenticate， 将 Authentication 对象传递给 ProviderManager。
        //DaoAuthenticationProvider 处理认证
        //调用 UserDetailsService.loadUserByUsername 加载用户信息。
        // PasswordEncoder 校验密码。
        //如果认证成功，返回一个新的 Authentication 对象（如带有权限信息的
        //将 Authentication 存储到 SecurityContextHolder
        //存储到 SecurityContextHolder 后，Spring Security 会根据 Authentication 对象中的权限信息（GrantedAuthority）来控制用户对资源的访问。
        //每个请求都会被过滤器链拦截，FilterSecurityInterceptor 会检查当前用户的权限是否允许访问目标资源。
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return authentication.isAuthenticated()? jwtService.generateToken(user.getUsername()) : "failure";
    }
}
