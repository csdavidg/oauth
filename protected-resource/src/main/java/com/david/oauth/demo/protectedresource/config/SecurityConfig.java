package com.david.oauth.demo.protectedresource.config;

import com.david.oauth.demo.protectedresource.filter.AccessTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.annotation.Resource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserSecurityConfig userSecurityConfig;

    @Value("${jwt.secret}")
    private String jwtKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").hasRole("admin")
                .anyRequest().authenticated()
                .and().requestCache().requestCache(new NullRequestCache())
                .and().httpBasic()
                .and()
                .csrf().disable();

        http.headers().frameOptions().disable();
        http.addFilter(new AccessTokenFilter(authenticationManager(), jwtKey));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManagerConfigurer managerConfigurer = auth.inMemoryAuthentication();
        managerConfigurer.withUser(userSecurityConfig.getUsername()).password("{noop}" + userSecurityConfig.getPassword())
                .roles(userSecurityConfig.getRole());
    }

}
