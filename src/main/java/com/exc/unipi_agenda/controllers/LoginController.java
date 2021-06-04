package com.exc.unipi_agenda.controllers;

//import com.exc.unipi_agenda.model.Db;
import com.exc.unipi_agenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;

import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/")
public class LoginController {

    @GetMapping(path = "/")
    public String getContent(Model model) {
        return "index";
    }

    @PostMapping("/")
    public Object loginUser(Model model,
                            HttpSession session,
                            @RequestParam(name = "username", required = false) String username,
                            @RequestParam(name = "password", required = false) String password)
    {
        User registeredUser = User.login(username,password,model);
        if (registeredUser != null){
            //System.out.println(registeredUser.getFullName());
            session.setAttribute("user", registeredUser);
            return new RedirectView("user");
        }
        model.addAttribute("message", "Something went wrong");
        return "index";
    }
}
