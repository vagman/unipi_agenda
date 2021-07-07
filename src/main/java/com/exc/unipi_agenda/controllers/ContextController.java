package com.exc.unipi_agenda.controllers;


import com.exc.unipi_agenda.model.Db;
import com.exc.unipi_agenda.model.UserNotification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContextController {

    public List<UserNotification> refreshesNotifications(String username){
        List<UserNotification> notificationList = new ArrayList<>();
        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }
        // load name
        String sql_query = "SELECT id_notification,msg, date, viewed FROM user_notification \n" +
                "WHERE username = ? AND date > NOW() - INTERVAL 1 MONTH \n" +
                "order by date;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                notificationList.add(new UserNotification(rs.getInt("id_notification"),
                                                          rs.getString("msg"),
                                                          rs.getDate("date"),
                                                          rs.getBoolean("viewed")));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return notificationList;
    }


}
