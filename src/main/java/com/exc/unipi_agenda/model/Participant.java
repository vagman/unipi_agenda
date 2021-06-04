package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Participant extends User{
    public Participant(String username) {
        super(username);
    }
    public boolean leave(String id, Participant p, Model model){
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "DELETE FROM meeting_participants WHERE id_meeting =? and username=?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1,id);
                ps.setString(1,p.getUsername());
                return ps.execute();
            }catch (SQLException throwables) {
                model.addAttribute("error","Something wrong with database");
                throwables.printStackTrace();
            }
        }
        return false;
    }
    public boolean meetingRequest(String id, String status, Model model) {
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "UPDATE meeting_participants SET invitation_status = ? WHERE id_meeting =? and username=?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1,status);
                ps.setString(2,id);
                ps.setString(3,this.getUsername());
                return ps.execute();
            }catch (SQLException throwables) {
                model.addAttribute("error","Something wrong with database");
                throwables.printStackTrace();
            }
        }
        return false;
    }
}
