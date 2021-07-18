package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserNotification {
    //private User user;
    private final int idNotification;
    private final String message;
    private final String datetime;

    public UserNotification(int idNotification, String message, String datetime, boolean viewed){
        this.idNotification = idNotification;
        this.message = message;
        this.datetime = datetime;
        this.viewed = viewed;
    }

    public int getIdNotification() {
        return idNotification;
    }
    public String getMessage() {
        return message;
    }
    public String getDatetime() {
        return datetime;
    }
    public boolean isViewed() {
        return viewed;
    }
    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
    private boolean viewed;
    public static boolean markAsViewed(String username){
        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }
        String sql_query = "UPDATE user_notification SET viewed=true WHERE username =?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,username);
            return ps.execute();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static List<Object> loadNotifications(String username){
        List<Object> notificationList = new ArrayList<>();
        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }
        // load all the notifications (invitations and notifications)
        String sql_query = "(SELECT concat('invitation') as type, m.id_meeting as id,m.name as meeting_name ," +
                "                   invitation_status, meeting_participants.date as date_add, null as msg, null as viewed, m.admin as meeting_admin\n" +
                "    FROM meeting_participants inner join meeting m on meeting_participants.id_meeting = m.id_meeting\n" +
                "    WHERE username = ?)\n" +
                "UNION\n" +
                "(SELECT concat('notification') as type, id_notification as id, null as meeting_name, " +
                "                   null as invitation_status, date as date_add, msg, viewed, null as meeting_admin\n" +
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
                            new Meeting(rs.getInt("id"),rs.getString("meeting_name"), new Admin(rs.getString("meeting_admin"))),
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
}