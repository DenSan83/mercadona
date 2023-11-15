package com.devdensan.mercadona.service;
import com.devdensan.mercadona.model.Role;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.repository.RoleRepository;
import com.devdensan.mercadona.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public int countUsers() {
        List<User> users = userRepository.findAll();
        return users.size();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersById() {
        return userRepository.findAllByUserId();
    }

    public boolean userExists(String username) {
        return userRepository.existsByUserName(username);
    }

    public User newUser(HttpServletRequest request) {
        Role role = roleRepository.findById(Integer.valueOf(request.getParameter("role"))).orElse(null);
        if (role == null) {
            return null;
        }

        User user = new User();
        user.setUserName(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setRole(role);
        if (!request.getParameter("password").isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getParameter("password")));
        }

        return userRepository.save(user);
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User editUser(int userId, String username, String email, int roleId, String password) {
        User user = getUserById(userId);
        if (user == null) {
            return null;
        }

        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return null;
        }
        user.setUserName(username);
        user.setEmail(email);
        user.setRole(role);
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return userRepository.save(user);
    }


    public boolean deleteUser(int userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
