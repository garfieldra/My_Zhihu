package com.example.myzhihu.config;

import com.example.myzhihu.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    /*
     * 配置密码加密方式
     * 我们使用 BCrypt 算法来对用户密码进行加密
     * 好处：相同明文每次加密结果不同，更安全
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /*
     * 配置 AuthenticationManager
     * 这个类负责管理认证（比如登录时验证用户名和密码）
     * 在登录时我们会用到它
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * 配置 Spring Security 的过滤链
     * 这里定义了哪些请求需要认证，哪些可以匿名访问
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                // 关闭 CSRF（跨站请求伪造），因为我们是 REST API，不用表单登录
                .csrf(csrf -> csrf.disable())

                // 配置请求的访问规则
                .authorizeHttpRequests(auth -> auth
                        // 登录、注册接口允许匿名访问
                        .requestMatchers("/api/auth/**").permitAll()
                        // 其他接口都需要认证，即登录后才能访问
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 最终构建SecurityFilterChain对象
        return http.build();
    }

}
