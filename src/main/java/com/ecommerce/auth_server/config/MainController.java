package com.ecommerce.auth_server.config;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/login/oauth2/code/client**")
    public String code() {
        return "code";
    }
}
