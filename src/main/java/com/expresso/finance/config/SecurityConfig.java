package com.expresso.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Este método "ensina" o Spring a criar o Bean
        // que o @Autowired no ClienteService precisa.
        return new BCryptPasswordEncoder();
    }
}