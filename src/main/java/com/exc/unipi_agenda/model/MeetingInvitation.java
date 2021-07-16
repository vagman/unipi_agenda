package com.exc.unipi_agenda.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeetingInvitation {
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Meeting meeting;
    private String date;
    private String status; // open, approved, deleted

    public MeetingInvitation(Meeting m, String date, String status){
        this.meeting = m;
        this.date = date;
        this.status = status;
    }

    public static boolean response(int id_meeting, String username, String response){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }
        // load name
        String sql_query = "UPDATE meeting_participants                      \n"+
                           "SET invitation_status = ?, date = NOW() \n"+
                           "WHERE id_meeting = ? and username = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,response);
            ps.setInt(2, id_meeting);
            ps.setString(3, username);
            return ps.execute();

        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
