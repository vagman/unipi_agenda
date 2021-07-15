package com.exc.unipi_agenda.controllers;


import com.exc.unipi_agenda.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContextController {

    public List<Object> refreshesNotifications(String username){
        List<Object> notificationList = new ArrayList<>();
        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }
        // load all the notifications (invitations and notifications)
        String sql_query = "(SELECT concat('invitation') as type, m.id_meeting as id,m.name as meeting_name ," +
                "                   invitation_status, meeting_participants.date as date_add, null as msg, null as viewed\n" +
                            "    FROM meeting_participants inner join meeting m on meeting_participants.id_meeting = m.id_meeting\n" +
                            "    WHERE username = ?)\n" +
                            "UNION\n" +
                            "(SELECT concat('notification') as type, id_notification as id, null as meeting_name, " +
                "                   null as invitation_status, date as date_add, msg, viewed\n" +
                            "    FROM user_notification\n" +
                            "    WHERE username = ?) order by date_add;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,username);
            ps.setString(2,username);
//            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getString("type").equals("notification")){
                    notificationList.add(new UserNotification(rs.getInt("id"),
                            rs.getString("msg"),
                            rs.getString("date_add"),
                            rs.getBoolean("viewed")));
                }else if(rs.getString("type").equals("invitation")){
                    notificationList.add(new MeetingInvitation(
                            new Meeting(rs.getInt("id"),rs.getString("meeting_name")),
                            rs.getString("date_add"),
                            rs.getString("invitation_status")));
                }
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
        String sql_query = "SELECT meeting.id_meeting,name,meeting.date,duration,admin, username, meeting.description\n" +
                           "FROM meeting left join meeting_participants on meeting.id_meeting = meeting_participants.id_meeting\n" +
                           "WHERE meeting.date > now() AND (admin = ? OR (username = ? AND invitation_status = 'approved'))\n" +
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
                        m.setDescription(rs.getString("description"));
                        m.setDatetime(rs.getDate("date"));
                        m.setDuration(rs.getString("duration"));
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
}
