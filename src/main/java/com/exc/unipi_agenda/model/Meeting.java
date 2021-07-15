package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting implements Serializable {

    private int id;
    private String name;
    private Date datetime;
    private String duration;
    private Admin admin;
    private String description;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    private List<Participant> participants = new ArrayList<>();
    private List<MeetingComment> comments = new ArrayList<>();

    //constructors
    public Meeting(int id, String name){
        this.id = id;this.name = name;
    }
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

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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

    // main actions
    public boolean create(User administrator) {
        Connection conn = Db.getConnection();

        if (conn == null) {
            return false;
        }

        // Get next id
        this.id = Meeting.getNewId();

        // Date formating
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String meetingDateString = dateFormatter.format(this.datetime);

        // Set admin
        this.admin = new Admin(administrator.getUsername());

        String sql_query = "INSERT INTO meeting VALUES (?,?,?,?,?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setInt(1, this.id);
            ps.setString(2, this.name);
            ps.setString(3, this.description);
            ps.setString(4, meetingDateString);
            ps.setString(5, this.duration);
            ps.setString(6, admin.getUsername());
            return ps.execute();
        } catch (SQLException throwables) {
            return false;
        }

    }

    public static int getNewId(){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return -1;
        }
        String sql_query = "select max(id_meeting)+1 as last_id from meeting;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("last_id");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
        return -1;
    }

    public void loadMessageComments(){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return;
        }

        String sql_query = "SELECT * FROM meeting_comments WHERE id_meeting = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setInt(1, this.id);
            ResultSet rs = ps.executeQuery();
            List<MeetingComment> meetingCommentList = new ArrayList<>();
            while(rs.next()){
                meetingCommentList.add(new MeetingComment(
                    rs.getInt("id_meeting"),
                    new User(rs.getString("username")),
                    rs.getString("msg")
                ));
            }
            this.comments = meetingCommentList;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
