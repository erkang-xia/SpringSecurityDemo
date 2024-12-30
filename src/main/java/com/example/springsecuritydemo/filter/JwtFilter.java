package com.example.springsecuritydemo.filter;

import com.example.springsecuritydemo.service.JwtService;
import com.example.springsecuritydemo.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // 检查 Authorization 头是否存在且符合规范
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String jwtToken = authorization.substring("Bearer ".length());

                // 对 JWT Token 进行验证和解析
                String username = jwtService.extractUsername(jwtToken);

                //not already authed
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 验证 Token 是否有效
                    if (jwtService.validateToken(jwtToken)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        // 构造 Authentication 对象
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, jwtService.getAuthorities(jwtToken)
                        );

                        //Authentication 对象的 额外请求上下文信息，例如客户端的 IP 地址、Session ID 等。这些信息可以用于记录审计日志或进一步的安全校验。
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 设置安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // 处理 token 验证或解析的异常
                logger.error("JWT Token 验证失败", e);
            }
        }

        // 将请求继续传递到下一个过滤器
        filterChain.doFilter(request, response);
    }
}
