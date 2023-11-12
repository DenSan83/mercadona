package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    private final AuthenticationService authenticationService;

    public AdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    @RequestMapping(value = { "", "/" })
    public String dashboard(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "dashboard");

        return "back/components/template";
    }
    @GetMapping
    @RequestMapping(value = { "products", "products/" })
    public String products(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "products");

        return "back/components/template";
    }
    @GetMapping
    @RequestMapping(value = { "categories", "categories/" })
    public String categories(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "categories");

        return "back/components/template";
    }
    @GetMapping
    @RequestMapping(value = { "users", "users/" })
    public String users(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "users");

        return "back/components/template";
    }

}
