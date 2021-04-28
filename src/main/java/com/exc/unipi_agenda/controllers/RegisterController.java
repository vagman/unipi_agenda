package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @GetMapping("/register")
    public String getContent(Model model) {
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(Model model,
                               @RequestParam(name = "username", required = false) String username,
                               @RequestParam(name = "password", required = false) String password,
                               @RequestParam(name = "firstname", required = false) String firstname,
                               @RequestParam(name = "lastname", required = false) String lastname) {
//        User u = User.register(username,password,firstname,lastname,model);
        System.out.println(firstname+lastname+username+password);
        return "signup";
//        if (u != null){
//            // TODO: 26/4/2021 after registration go to main page
////            model.addAttribute("message", "all ok");
//            return "signup";
//        }else {
//            return "signup";
//        }


    }
}
