package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting {

    private int id;
    private String name;
    private Date datetime;
    private float duration;
    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    private List<Participant> participants = new ArrayList<>();
    private List<MeetingComment> comments = new ArrayList<>();

    //constructors
    public Meeting(int id){
        this.id = id;
    }
    public Meeting(){}
//    public Meeting(Admin admin) {
//        this.admin = admin;
//    }
//    public Meeting(String id,Admin admin,String name, float duration){
//        this.id = id;
//        this.admin = admin;
//    }
//    public Meeting(String id, String admin) {
//        this.id = id;
//        this.admin = admin;
//    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public Admin getAdmin() {
        return admin;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<MeetingComment> getComments() {
        return comments;
    }

    public void setComments(List<MeetingComment> comments) {
        this.comments = comments;
    }
    //    main actions
    public boolean create(String name, User admin, String date, float duration, Model model) {
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "INSERT INTO meeting(name, date, duration, admin) VALUES (?,?,?,?);";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1, name);
                ps.setString(2, date);
                ps.setFloat(3, duration);
                ps.setString(4, admin.getUsername());
                return ps.execute();
            } catch (SQLException throwables) {
                model.addAttribute("error", "Something wrong with database");
                throwables.printStackTrace();
            }
        }
        return false;
    }

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



}
