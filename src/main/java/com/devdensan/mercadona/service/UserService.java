package com.devdensan.mercadona.service;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int countUsers() {
        List<User> users = userRepository.findAll();
        return users.size();
    }
}
