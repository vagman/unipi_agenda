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

    public Meeting(String meetingTitle, String meetingDescription, Date meetingDate, String meetingDuration) {
        this.name = meetingTitle;
        this.datetime = meetingDate;
        this.duration = meetingDuration;
        this.description = meetingDescription;
    }

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
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
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

        String sql_query = "SELECT * FROM meeting_comments WHERE id_meeting = ? order by date desc";
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
    public static List<Meeting> loadMeetings(String username){
        List<Meeting> meeting = new ArrayList<>();
        Connection conn = Db.getConnection();
//      we get meetings data from the meetings which the user is admin or participant
        String sql_query = "SELECT meeting.id_meeting,name,meeting.date,duration,admin, username, invitation_status, meeting.description\n" +
                "FROM meeting left join meeting_participants on meeting.id_meeting = meeting_participants.id_meeting\n" +
                "WHERE meeting.date > now() AND\n" +
                "      (meeting.admin = ? OR (meeting_participants.username = ? AND meeting_participants.invitation_status = 'approved'))\n" +
                "ORDER BY date;";
        try {
            if (conn!=null) {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1, username);
                ps.setString(2, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    try {
//                        if the meeting is the same
//                          the query can return one or more rows for the same meeting
//                          it depends on the participants
                        if ((meeting.get(meeting.size() - 1).getId() == rs.getInt("id_meeting"))){
                            if (rs.getString("invitation_status").equals("approved")){
                                meeting.get(meeting.size() - 1).getParticipants().add(new Participant(rs.getString("username")));
                            }
                        }else{
//                            if is a new meeting throw an exception to create new meeting
                            throw new IndexOutOfBoundsException();
                        }
                    }catch (IndexOutOfBoundsException e){
//                      If is a new meeting or is the first time create a meeting object and add it to the list
                        Meeting m = new Meeting(rs.getInt("id_meeting"));
                        m.setName(rs.getString("name"));
                        m.setDescription(rs.getString("description"));
                        m.setDatetime(rs.getDate("date"));
                        m.setDuration(rs.getString("duration"));
                        m.setAdmin(new Admin(rs.getString("admin")));
//                        check if the meeting has participants
                        if (rs.getString("username") != null){
                            if (rs.getString("invitation_status").equals("approved")){
                                m.getParticipants().add(new Participant(rs.getString("username")));
                            }
                        }
                        meeting.add(m);
                    }

                }
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return meeting;
    }
}
