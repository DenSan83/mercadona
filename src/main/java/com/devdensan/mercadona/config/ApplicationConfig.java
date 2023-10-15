package com.devdensan.mercadona.config;

import com.devdensan.mercadona.model.Role;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.repository.RoleRepository;
import com.devdensan.mercadona.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = repository.findByUserName(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return user;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner defaultsCreator(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role adminRole = new Role(1,"ADMIN");
            Role userRole = new Role(2,"USER");
            roleRepository.saveAll(
                    List.of(adminRole, userRole)
            );

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User root = new User("root", encoder.encode("root"), adminRole, null);
            userRepository.save(root);
        };
    }


}
