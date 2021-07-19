package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.*;
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
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return null;
        }
        String sqlQuery = "SELECT username FROM users WHERE username LIKE ? AND username != ?;";
        List<Object> searchResults = new ArrayList<>();
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


        return MeetingComment.send(idMeeting,username,messageText);
    }


    @PostMapping("/update-meeting-description")
    public boolean updateMeetingDescription(Model model,
                                            HttpSession session,
                                            @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                            @RequestParam(name = "meeting_description", required = false) String meetingDescription
    )
    {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }
//      find the meeting
        for (Meeting m:registedUser.getMeetings()){
            if (m.getAdmin().getUsername().equals(registedUser.getUsername())){
//                if update statement was completed successfully refresh the meetings
                if (m.getAdmin().update(idMeeting,meetingDescription)){
                    registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/update-meeting-title")
    public boolean updateMeetingTitle(Model model,
                                            HttpSession session,
                                            @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                            @RequestParam(name = "meeting_title", required = false) String meetingTitle
    )
    {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }
//      find the meeting
        for (Meeting m:registedUser.getMeetings()){
            if (m.getAdmin().getUsername().equals(registedUser.getUsername())){
//                if update statement was completed successfully refresh the meetings
                if (m.getAdmin().updateTitle(idMeeting,meetingTitle)){
                    registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/update-meeting-date")
    public boolean updateMeetingDate(Model model,
                                            HttpSession session,
                                            @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                            @RequestParam(name = "meeting_date", required = false) String meetingDate
    )
    {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }
//      find the meeting
        for (Meeting m:registedUser.getMeetings()){
            if (m.getAdmin().getUsername().equals(registedUser.getUsername())){
//                if update statement was completed successfully refresh the meetings
                if (m.getAdmin().updateDate(idMeeting,meetingDate)){
                    registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/delete-participant")
    public boolean updateParticipants(HttpSession session,
                                      @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                      @RequestParam(name = "participant-username", required = false) String participantUsername
    )
    {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }
//      find the meeting
        for (Meeting m:registedUser.getMeetings()){
            if (m.getAdmin().getUsername().equals(registedUser.getUsername())){
//                if update statement was completed successfully refresh the meetings
                if (m.getAdmin().deleteParticipant(idMeeting,participantUsername)){
                    registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
                    return true;
                }
            }
        }
        return false;
    }

//    @PostMapping("/invitation_response")
//    public boolean InvitationResponse(Model model,
//                                           HttpSession session,
//                                            @RequestParam(name = "response", required = false) String response,
//                                           @RequestParam(name = "id_meeting", required = false) int id_meeting) {
//
//        User registedUser = (User)session.getAttribute("user");
//        if(registedUser == null){
//            return false;
//        }
//        if (MeetingInvitation.response(id_meeting,registedUser.getUsername(),response)){
//            registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
//            return true;
//        }
//        return false;
//    }
    @PostMapping("/viewed")
    public boolean Viewed(HttpSession session) {
        // if user not registered
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }
        if (UserNotification.markAsViewed(registedUser.getUsername())){
            registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
            return true;
        }
        return false;
    }

    @PostMapping("/add-participant-to-meeting")
    public boolean addParticipantToMeeting(HttpSession session,
                                           @RequestParam(name = "participantUsername", required = false) String participantUsername,
                                           @RequestParam(name = "idMeeting", required = false) int idMeeting,
                                           @RequestParam(name = "meetingAdmin", required = false) String meetingAdmin) {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }

        Meeting currentMeeting = new Meeting(idMeeting);
        currentMeeting.setAdmin(new Admin(meetingAdmin));

        String[] participantList = {participantUsername};
        currentMeeting.getAdmin().addParticipants(currentMeeting.getId(),participantList);

        return true;
    }

    @PostMapping("/remove-participant")
    public boolean removeMeetingParticipant(HttpSession session,
                                           @RequestParam(name = "id_meeting", required = false) int idMeeting,
                                            @RequestParam(name = "meeting_admin", required = false) String meetingAdmin,
                                            @RequestParam(name = "participant_username", required = false) String participantUsername
                                            ) {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return false;
        }

        Meeting currentMeeting = new Meeting(idMeeting);
        currentMeeting.setAdmin(new Admin(meetingAdmin));

        currentMeeting.getAdmin().deleteParticipant(currentMeeting.getId(), participantUsername);

        return true;
    }
}
