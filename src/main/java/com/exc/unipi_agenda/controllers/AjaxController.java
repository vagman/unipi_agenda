package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.Db;
import com.exc.unipi_agenda.model.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController extends ContextController{
    @PostMapping("/search-user")
    public List<Object> searchUser(Model model,
                                   HttpSession session,
                                   @RequestParam(name = "search_query", required = false) String search_query) {

        Connection conn = Db.getConnection();
        if (conn == null) {
            return null;
        }

        String sqlQuery = "SELECT username FROM users WHERE username LIKE ? AND username != ?;";
        List<Object> searchResults = new ArrayList<Object>();
        User registedUser = (User)session.getAttribute("user");
        try {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1,"%"+search_query+"%");
            ps.setString(2,registedUser.getUsername());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String result_username = rs.getString("username");
                searchResults.add(new User(result_username));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return searchResults;
    }

    @PostMapping("/send-meeting-message")
    public boolean sendMeetingMessage(Model model,
                                      HttpSession session,
                                      @RequestParam(name = "message_text", required = false) String messageText,
                                      @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                      @RequestParam(name = "username", required = false) String username) {

        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }

        String sql_query = "INSERT INTO meeting_comments VALUES(?, ?, NOW(), ?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setInt(1,idMeeting);
            ps.setString(2,username);
            ps.setString(3,messageText);
            ps.execute();
            conn.close();


            return true;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    @PostMapping("/update-meeting-description")
    public boolean updateMeetingDesctiprion(Model model,
                                           HttpSession session,
                                           @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                           @RequestParam(name = "meeting_description", required = false) String meetingDescription
    )
    {
        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }

        String sql_query = "UPDATE meeting SET description = ? WHERE id_meeting = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,meetingDescription);
            ps.setInt(2,idMeeting);
            ps.execute();
            conn.close();

            // Update the meeting list that is stored on the session
            User registedUser = (User)session.getAttribute("user");
            if(registedUser == null){
                return false;
            }
            registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));

            return true;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @PostMapping("/leave-meeting")
    public boolean leaveMeeting(Model model,
                                            HttpSession session,
                                            @RequestParam(name = "id_meeting", required = true) int idMeeting
    )
    {
        //TODO: Leave meeting
        return true;
    }

    /*@PostMapping("/invitation_response")
    public boolean InvitationResponse(Model model,
                                           HttpSession session,
                                           @RequestParam(name = "response", required = false) String response,
                                           @RequestParam(name = "id_meeting", required = false) int id_meeting) {

        Connection conn = Db.getConnection();
        if (conn == null) {
            return false;
        }

        String sql_query = "UPDATE meeting_participants SET invitation_status = ?, date = NOW() " +
                            "WHERE id_meeting = ? AND username = ?";
        List<Object> search_results = new ArrayList<Object>();
        User registedUser = (User)session.getAttribute("user");
        try {
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1,response);
            ps.setInt(2,id_meeting);
            ps.setString(3,registedUser.getUsername());
            boolean result = ps.execute();
            conn.close();
            registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
            return result;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }*/
}
