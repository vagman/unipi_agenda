package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Admin extends User{
    public Admin(String username) {
        super(username);
    }
    public boolean update(String name, int id, String date, float duration, Model model){
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "UPDATE meeting SET name=?,date=?,duration=? WHERE id_meeting =?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1,name);
                ps.setString(2,date);
                ps.setFloat(3,duration);
                ps.setInt(4,id);
                return ps.execute();
            }catch (SQLException throwables) {
                model.addAttribute("error","Something wrong with database");
                throwables.printStackTrace();
            }
        }
        return false;
    }
    public boolean delete(int id, Model model){
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "DELETE FROM meeting WHERE id_meeting =?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setInt(1,id);
                return ps.execute();
            }catch (SQLException throwables) {
                model.addAttribute("error","Something wrong with database");
                throwables.printStackTrace();
            }
        }
        return false;
    }
    public void addParticipants(int id, String[] participants, Model model) {
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "INSERT INTO meeting_participants(id_meeting, username, invitation_status,date) VALUES (?,?,?,NOW());";
            try {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(sql_query);
                for (String p : participants) {
                    ps.setInt(1, id);
                    ps.setString(2, p);
                    ps.setString(3, "open");
                    ps.addBatch();
                }
                conn.commit();
                ps.executeBatch();
            } catch (SQLException throwables) {
                model.addAttribute("error", "Something wrong with database");
                throwables.printStackTrace();
            }
        }
    }

}
