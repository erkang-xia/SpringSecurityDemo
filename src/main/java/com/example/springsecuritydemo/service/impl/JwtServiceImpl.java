package com.example.springsecuritydemo.service.impl;


import com.example.springsecuritydemo.service.JwtService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 随机生成一个密钥

    @Override
    public String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60; // 1小时 (以毫秒为单位)

        return Jwts.builder()
                .setSubject(username) // 设置主题 (用户名)
                .setIssuedAt(new Date()) // 设置生成时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 设置过期时间
                .signWith(key) // 使用密钥签名
                .compact(); // 生成JWT字符串
    }

    @Override
    public String extractUsername(String jwtToken) {
        try {
            // 从 Token 中解析出主题（通常为用户名）
            return Jwts.parser()
                    .verifyWith((SecretKey) key) // 设置解析用的密钥
                    .build()
                    .parseSignedClaims(jwtToken) // 解析 JWT
                    .getPayload()
                    .getSubject(); // 获取主题 (用户名)
        } catch (Exception e) {
            // 捕获异常并记录日志
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    @Override
    public boolean validateToken(String jwtToken) {
        try {
            // 验证 JWT 是否合法
            Jwts.parser()
                    .verifyWith((SecretKey) key) // 使用密钥解析
                    .build()
                    .parseSignedClaims(jwtToken); // 解析 JWT
            return true; // 如果没有抛出异常，则验证通过
        } catch (Exception e) {
            // 捕获任何异常并记录日志
            System.err.println("Token validation failed: " + e.getMessage());
            return false; // 如果解析失败，返回 false
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(String jwtToken) {
        return List.of();
    }
}