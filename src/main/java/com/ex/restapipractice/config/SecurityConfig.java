package com.ex.restapipractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {
  @Bean
  UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

    UserDetails newUser = User.withUsername("user")
        .password(passwordEncoder().encode("1234"))
        .authorities("read")
        .build();

    userDetailsManager.createUser(newUser);
    return userDetailsManager;
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
