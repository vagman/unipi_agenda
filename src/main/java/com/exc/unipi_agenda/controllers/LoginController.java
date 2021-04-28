package com.exc.unipi_agenda.controllers;

//import com.exc.unipi_agenda.model.Db;
import com.exc.unipi_agenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;

import org.springframework.ui.Model;

@Controller
@RequestMapping(path = "/")
public class LoginController {

    @GetMapping(path = "/")
    public String getContent(Model model) {
        return "index";
    }

    @PostMapping("/")
    public String loginUser(Model model,
                            @RequestParam(name = "username", required = false) String username,
                            @RequestParam(name = "password", required = false) String password){
        User u = User.login(username,password,model);
        if (u != null){
//          TODO: next page with all the meetings
            model.addAttribute("message", "The user "+u.get_Username()+" connected successfully");
        }
        return "index";
    }
}
