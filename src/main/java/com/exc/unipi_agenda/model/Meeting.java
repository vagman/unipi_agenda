package com.exc.unipi_agenda.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Meeting {
    private final String id;
    private String name;
    private Date datetime;
    private float duration;
    private final String admin;
    private List<User> participants;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getAdmin() {
        return admin;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void addParticipant(String username) {
        Connection conn = Db.getConnection();
        if (conn == null) {
            return ;
        }

        try {
            String sql_query = "INSERT INTO meeting_participants VALUES (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,this.id);
            ps.setString(2,username);
            ps.setString(3,"pending");
            int rows = ps.executeUpdate();
        } catch (SQLException e) {
            return;
        }
    }

    public List<MeetingComment> getComments() {
        return comments;
    }

    public void setComments(List<MeetingComment> comments) {
        this.comments = comments;
    }

    private List<MeetingComment> comments;

    public static String getNewId(){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return "";
        }
        String sql_query = "select max(id_meeting)+1 as last_id from meeting;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("last_id");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return "";
        }
        return "";
    }


    public Meeting(String id, String admin) {
        this.id = id;
        this.admin = admin;
    }

    public void add(){

    }
    public void update(){}
    public void delete(){}
    public void addParticipants(List<User> participants){}
}
