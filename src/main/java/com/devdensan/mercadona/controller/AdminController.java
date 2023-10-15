package com.devdensan.mercadona.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    @GetMapping
    @RequestMapping(value = { "", "/" })
    public String dashboard() {
        // TODO: create dashboard
        return "Welcome to admin dashboard";
    }

}
