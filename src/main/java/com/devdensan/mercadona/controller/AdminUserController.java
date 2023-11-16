package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.Role;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.service.RoleService;
import com.devdensan.mercadona.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "admin/user")
public class AdminUserController {
    private final AuthenticationService authenticationService;
    private final RoleService roleService;
    private final UserService service;

    public AdminUserController(AuthenticationService authenticationService, RoleService roleService, UserService userService) {
        this.authenticationService = authenticationService;
        this.roleService = roleService;
        this.service = userService;
    }

    @GetMapping("new")
    public String userForm(Model model) {
        authenticationService.loadConnectedUser(model);

        // Page data
        model.addAttribute("page", "user-form");
        model.addAttribute("roles", roleService.getAllRoles());

        return "back/components/template";
    }

    @PostMapping("new")
    public String userAdd(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map<String, String> message = new HashMap<>();

        // Verifying logic
        if (!validateUserInput(true, request, redirectAttributes, message)) {
            return "redirect:/admin/user/new";
        }

        // Create new user
        User user = service.newUser(request);
        if (user == null) {
            message.put("type", "danger");
            message.put("text", "Erreur lors de l'enregistrement de l'utilisateur");
        } else {
            message.put("type", "success");
            message.put("text", "Utilisateur ajouté correctement.");
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable int userId, Model model, RedirectAttributes redirectAttributes) {
        authenticationService.loadConnectedUser(model);

        User user = service.getUserById(userId);
        if (user == null) {
            Map<String, String> message = new HashMap<>();
            message.put("type", "danger");
            message.put("text", "Utilisateur inéxistant.");
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/admin/users";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("connectedUser", authenticationService.getAuthenticatedUser());
        }

        model.addAttribute("page", "user-form");
        return "back/components/template";
    }

    @PostMapping("/edit/{userId}")
    public String saveEditedUser(@PathVariable int userId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = service.getUserById(userId);

        // Verify existant
        Map<String, String> message = new HashMap<>();
        message.put("type", "danger");
        if (user == null) {
            message.put("text", "Utilisateur inéxistant.");
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/admin/users";
        }

        // Verifying logic
        if (!validateUserInput(false, request, redirectAttributes, message)) {
            return "redirect:/admin/user/edit/" + userId;
        }

        // User edition
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        int roleId = Integer.parseInt(request.getParameter("role"));
        String password = request.getParameter("password");
        User editedUser = service.editUser(userId, username, email, roleId, password);

        if (editedUser == null) {
            message.put("text", "Erreur lors de l'enregistrement de l'utilisateur");
        } else {
            message.put("type", "success");
            message.put("text", "Utilisateur modifié correctement.");
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam("user-id") int userId, RedirectAttributes redirectAttributes) {
        Map<String, String> message = new HashMap<>();
        message.put("type", "danger");

        // Verify if exists
        User user = service.getUserById(userId);
        if (user == null) {
            message.put("text", "Erreur : utilisateur non trouvé ou inéxistant.");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        // If self: block
        User connectedUser = authenticationService.getAuthenticatedUser();
        if (connectedUser == user) {
            message.put("text", "Erreur : l'utilisateur à supprimer est encore connecté.");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        boolean deleted = service.deleteUser(userId);
        if (deleted) {
            message.put("type", "success");
            message.put("text", "Utilisateur supprimé correctement.");
        } else {
            message.put("text", "Erreur lors de la suppression de l'utilisateur.");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean validateUserInput(boolean isNew, HttpServletRequest request, RedirectAttributes redirectAttributes, Map<String, String> message) {
        // Verify user
        String username = request.getParameter("username");
        if (username.isEmpty()) {
            message.put("text", "Le nom d'utilisateur ne peut pas être vide.");
            redirectAttributes.addFlashAttribute("message", message);
            return false;
        }
        if (isNew && service.userExists(username)) {
            message.put("text", "Le nom d'utilisateur existe déjà.");
            redirectAttributes.addFlashAttribute("message", message);
            return false;
        }

        // Verify if email is valid
        String email = request.getParameter("email");
        if (!email.isEmpty() && !isValidEmail(email)) {
            message.put("text", "Format d'email incorrect.");
            redirectAttributes.addFlashAttribute("message", message);
            return false;
        }

        // Verify if role is valid and not empty
        List<Role> allRoles = roleService.getAllRoles();
        int roleNum = Integer.parseInt(request.getParameter("role"));
        Role selectedRole = allRoles.stream()
                .filter(role -> role.getRoleId() == roleNum)
                .findFirst()
                .orElse(null);
        if (selectedRole == null) {
            message.put("text", "Le rôle sélectionné n'est pas valide.");
            redirectAttributes.addFlashAttribute("message", message);
            return false;
        }

        // Verify passwords when not empty
        String password = request.getParameter("password");
        if (password != null && !password.equals(request.getParameter("password2"))) {
            message.put("text", "Les mots de passe ne correspondent pas.");
            redirectAttributes.addFlashAttribute("message", message);
            return false;
        }

        return true;
    }

}
