package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.Db;
import com.exc.unipi_agenda.model.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {
    @PostMapping("/search-user")
    public List<Object> searchUser(Model model,
                                   HttpSession session,
                                   @RequestParam(name = "search_query", required = false) String search_query) {

        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }

        String sql_query = "SELECT username FROM users WHERE username LIKE ? AND username != ?;";
        List<Object> search_results = new ArrayList<Object>();
        User registedUser = (User)session.getAttribute("user");
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,"%"+search_query+"%");
            ps.setString(2,registedUser.getUsername());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String result_username = rs.getString("username");
                search_results.add(new User(result_username));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return search_results;
    }
}
