package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.filter.JwtFilter;
import com.example.springsecuritydemo.service.UserDetailsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }


    //not recommended because insecure
//    @Bean
//    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().authenticated().and().httpBasic();
//
//        return http.build();
//    }

    //当一个 GET 请求被发出，流程大致如下
    //进入 Spring Security Filter Chain 所有进入的请求都会被 SecurityFilterChain 拦截。
    //JwtFilter 是链条中的第一个被执行的自定义过滤器
    //请求会到达 FilterSecurityInterceptor，它会根据你的 securityFilterChain 中的授权配置，判断请求是否被允许
    // 配置中通过 .authorizeHttpRequests 定义了哪些路径是公开的（如 /api/v1/user/register 和 /api/v1/user/login），以及哪些路径需要认证。

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer->customizer.disable());
        http.authorizeHttpRequests(request->request
                .requestMatchers("/api/v1/user/register  ","/api/v1/user/login")
                .permitAll()
                .anyRequest().authenticated());
        //启动基于表单的验证，用于交互式登入
        http.formLogin(Customizer.withDefaults());
        //Http basic 游览器弹对话框，还可以用于非交互式，比如postman
        http.httpBasic(Customizer.withDefaults());
        //取消session，每访问一个地方就要password
//        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        //生成 example 数据
//        UserDetails user1 = User.withDefaultPasswordEncoder().username("user1").password("password").roles("USER").build();
//        UserDetails user2 = User.withDefaultPasswordEncoder().username("user2").password("password").roles("ADMIN").build();
//        //use for demo, default implementation
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //不使用password encoder
//        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        //使用 PasswordEncoder 比较输入的密码和数据库中的加密密码
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        //会自动调用loadUserByUsername(username)从userDetailsService
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    //AuthenticationConfiguration 是 Spring Security 提供的配置类。
    //它会扫描 Spring 容器中的所有 AuthenticationProvider 并注册到 AuthenticationManager 中。
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }



}
