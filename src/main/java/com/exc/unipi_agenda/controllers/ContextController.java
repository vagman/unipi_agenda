package com.exc.unipi_agenda.controllers;


import com.exc.unipi_agenda.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContextController {

    public List<UserNotification> refreshesNotifications(String username){
        List<UserNotification> notificationList = new ArrayList<>();
        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }
        // load last month notifications
        String sql_query = "SELECT id_notification,msg, date, viewed FROM user_notification \n" +
                           "WHERE username = ? AND date > NOW() - INTERVAL 1 MONTH \n" +
                           "order by date;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                notificationList.add(new UserNotification(rs.getInt("id_notification"),
                                                          rs.getString("msg"),
                                                          rs.getDate("date"),
                                                          rs.getBoolean("viewed")));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return notificationList;
    }

    public List<Meeting> refreshesMeetings(String username){
        List<Meeting> meeting = new ArrayList<>();
        Connection conn = Db.getConnection();
//      we get meetings data from the meetings which the user is admin or participant
        String sql_query = "SELECT meeting.id_meeting,name,date,duration,admin, username\n" +
                           "FROM meeting left join meeting_participants on meeting.id_meeting = meeting_participants.id_meeting\n" +
                           "WHERE date > now() AND (admin = ? OR username = ?) AND invitation_status = 'approved'\n" +
                           "ORDER BY id_meeting;";
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
        return meeting;

    }

    public List<MeetingInvitation> refreshesInvitations(String username){
        List<MeetingInvitation> refreshesInvitations = new ArrayList<>();
        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }
        // load the 'open' invitations
        String sql_query = "SELECT id_meeting,name, date, invitation_status " +
                           "FROM meeting_participants natural join meeting\n" +
                           "WHERE username = ? AND invitation_status = 'open' \n" +
                           "order by date;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                refreshesInvitations.add(new MeetingInvitation(
                        new Meeting(rs.getInt("id_meeting"),rs.getString("name")),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return refreshesInvitations;
    }
}
