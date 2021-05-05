package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.User;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RegisterController {
    @GetMapping("/register")
    public String getContent(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public Object registerUser(Model model,
                               @RequestParam(name = "username", required = false) String username,
                               @RequestParam(name = "password", required = false) String password,
                               @RequestParam(name = "firstname", required = false) String firstname,
                               @RequestParam(name = "lastname", required = false) String lastname)
    {
        if(User.register(username, password, firstname, lastname, model) != null){
            Session s = new Session();

            return new RedirectView("user");
        }
        return "register";
    }
}
