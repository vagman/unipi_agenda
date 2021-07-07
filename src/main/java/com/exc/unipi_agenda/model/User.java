package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    public User(String username){
        this.username = username;
        this.loadAttributes();
    }

    private String username;
    private String firstName;
    private String lastName;
    private List<UserNotification> notificationList= new ArrayList<>();
    public String color;
    private List<Meeting> meetings;

    public List<MeetingInvitation> getMeetingInvitations() {
        return meetingInvitations;
    }

    public void setMeetingInvitations(List<MeetingInvitation> meetingInvitations) {
        this.meetingInvitations = meetingInvitations;
    }

    private List<MeetingInvitation> meetingInvitations;

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<UserNotification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<UserNotification> notificationList) {
        this.notificationList = notificationList;
    }

    public static User login(String username, String pass, Model model) {
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "SELECT password,password_salt,first_name,last_name FROM users " +
                            "WHERE username=?;";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1,username);
                ResultSet rs = ps.executeQuery();
//               if the username exists
                if (rs.next()){
                    String hash_password = rs.getString(1);
                    String password_salt = rs.getString(2);
                    Encryption e1 = new Encryption();
                    if (e1.passwordMach(hash_password,password_salt,pass)){
                        conn.close();
                        return new User(username);
                    }
                }
                conn.close();
                model.addAttribute("message","Username or password are not correct");
                return null;
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    public User logout(User u) {
        return null;
    }

    public static User  register(String username, String password, String firstName, String lastName, String color, Model model) {
        Connection conn = Db.getConnection();
        if (conn!=null){
            try {
//              prepare password hash
                Encryption encryption = new Encryption();
                encryption.encrypt(password);
//              prepare the query for the database
                String sql_query = "INSERT INTO users value (?,?,?,?,?,?);";
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1,username);
                ps.setString(2,encryption.getPassword_hashed());
                ps.setString(3,encryption.getSalt());
                ps.setString(4,firstName);
                ps.setString(5,lastName);
                ps.setString(6,color);
                int rows = ps.executeUpdate();
                if (rows>0){
                    model.addAttribute("message","New user created successfully");
                    return new User(username);
                }else {
                    model.addAttribute("message","Something happen (unknown)");
                    return null;
                }
            } catch (SQLException e) {
//                handle primary key exception and return the right message for the user
                if (e.getErrorCode() == Db.getMysqlDuplicatePkexception())
                    model.addAttribute("message","This username is already exists");
                else {
                    model.addAttribute("message","Something happen with Database");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void loadAttributes(){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return;
        }

        // load name
        String sql_query = "SELECT first_name,last_name,color FROM users WHERE username = ? ;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,this.username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.color = rs.getString("color");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
