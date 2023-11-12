package com.devdensan.mercadona.config;

import com.devdensan.mercadona.model.Role;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.repository.RoleRepository;
import com.devdensan.mercadona.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

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
