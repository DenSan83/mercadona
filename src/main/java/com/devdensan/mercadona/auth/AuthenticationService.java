package com.devdensan.mercadona.auth;

import org.springframework.ui.Model;
import com.devdensan.mercadona.model.Role;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.repository.RoleRepository;
import com.devdensan.mercadona.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        Role userRole = roleRepository.findByRoleName("USER"); // Default role for new user
        var user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();
        userRepository.save(user);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByUserName(username);
        }
        return null;
    }

    public void loadConnectedUser(Model model) {
        User connectedUser = getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());
    }
}
