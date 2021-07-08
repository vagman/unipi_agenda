package com.exc.unipi_agenda.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class MeetingComment {
    private User user;
    private String message;
    private String date;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    static boolean send(int id_meeting, String username, String message){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }
        // load name
        String sql_query = "INSERT INTO meeting_comments(id_meeting, username, date, msg)\n"+
                            "VALUES(?,?,NOW(),?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setInt(1,id_meeting);
            ps.setString(2, username);
            ps.setString(3, message);
            return ps.execute();

        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
