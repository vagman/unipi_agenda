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
    private String password;
    private String firstName;
    private String lastName;
    private List<UserNotification> notificationList= new ArrayList<>();
    public String color;


    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    private List<Meeting> meetings;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
            String sql_query = "SELECT password,password_salt FROM users " +
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
                        User u =new User(username);
                        u.loadMeetings();
                        u.loadNotifications();
                        return u;
                    }
                }
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

    private void loadMeetings(){
        List<Meeting> meeting = new ArrayList<>();
        Connection conn = Db.getConnection();
//      we get meetings data from the meetings which the user is admin or participant
        String sql_query = "SELECT meeting.id_meeting,name,date,duration,admin, username\n" +
                        "FROM meeting left join meeting_participants on meeting.id_meeting = meeting_participants.id_meeting\n" +
                        "WHERE date > now() AND admin = ? OR username = ?\n" +
                        "ORDER BY id_meeting;";
//        String sql_query = "SELECT meeting.id_meeting,name,date,duration,admin, username\n" +
//                "FROM meeting inner join meeting_participants on meeting_participants.id_meeting = meeting.id_meeting\n" +
//                "WHERE date > now() AND admin = ? OR username = ?\n" +
//                "ORDER BY id_meeting;";

        try {
            if (conn!=null) {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1, this.getUsername());
                ps.setString(2, this.getUsername());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    try {
//                        if the meeting is the same
//                          the query can return one or more rows for the same meeting
//                          it depends on the participants
                        if (meeting.get(meeting.size() - 1).getId() == rs.getInt("id_meeting")) {
                            meeting.get(meeting.size() - 1).getParticipants().add(new Participant(rs.getString("username")));
                        }else{
//                            if is a new meeting throw an exception to create new meeting
                            throw new IndexOutOfBoundsException();
                        }
                    }catch (IndexOutOfBoundsException e){
//                      If is a new meeting or is the first time create a meeting object and add it to the list
                        Meeting m = new Meeting(rs.getInt("id_meeting"));
                        m.setName(rs.getString("name"));
                        m.setDatetime(rs.getDate("date"));
                        m.setDuration(rs.getFloat("duration"));
                        m.setAdmin(new Admin(rs.getString("admin")));
                        m.getParticipants().add(new Participant(rs.getString("username")));
                        meeting.add(m);
                    }

                }
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.setMeetings(meeting);
    }


    private boolean loadAttributes(){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }

        // load name
        String sql_query = "SELECT * FROM users WHERE username = ? ;";
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
            return false;
        }


        // TODO: load notifications

        return true;
    }
    private boolean loadNotifications(){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }
        // load name
        String sql_query = "SELECT id_notification,msg, date, viewed FROM user_notification \n" +
                        "WHERE username = ? AND date > NOW() - INTERVAL 1 MONTH \n" +
                        "order by date;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,this.username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                notificationList.add(new UserNotification(rs.getInt("id_notification"),rs.getString("msg"),
                        rs.getDate("date"),rs.getBoolean("viewed")));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
