package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Participant extends User{
    public Participant(String username) {
        super(username);
    }
    public boolean leave(int id){
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "DELETE FROM meeting_comments WHERE id_meeting = ? and username= ?;" +
                               "DELETE FROM meeting_participants WHERE id_meeting =? and username=?;";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setInt(1,id);
                ps.setString(2,this.getUsername());
                ps.setInt(3,id);
                ps.setString(4,this.getUsername());
                return ps.execute();
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }
}
