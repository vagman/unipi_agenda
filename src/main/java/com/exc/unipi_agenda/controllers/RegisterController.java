package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.Random;

@Controller
public class RegisterController extends ContextController {
    @GetMapping("/register")
    public String getContent(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public Object registerUser(Model model,
                               HttpSession session,
                               @RequestParam(name = "username", required = false) String username,
                               @RequestParam(name = "password", required = false) String password,
                               @RequestParam(name = "firstname", required = false) String firstname,
                               @RequestParam(name = "lastname", required = false) String lastname)
    {
        //random color generation
        Random rand = new Random();
        Color randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        String randomColorHex = "#"+Integer.toHexString(randomColor.getRGB()).substring(2);

        User registeredUser = User.register(username, password, firstname, lastname, randomColorHex, model);
        if(registeredUser != null){
            registeredUser.setNotificationList(refreshesNotifications(registeredUser.getUsername()));
            registeredUser.setMeetings(refreshesMeetings(registeredUser.getUsername()));
            session.setAttribute("user", registeredUser);
            return new RedirectView("user");
        }
        return "register";
    }
}
