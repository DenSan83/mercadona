package com.devdensan.mercadona.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping
    @RequestMapping(value = { "login", "login/" })
    public String login() {
        return "back/login";
    }


}
