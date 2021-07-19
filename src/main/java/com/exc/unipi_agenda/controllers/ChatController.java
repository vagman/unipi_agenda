package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.Meeting;
import com.exc.unipi_agenda.model.MeetingComment;
import com.exc.unipi_agenda.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChatController extends ContextController{
    @GetMapping(path = "/chat")
    public Object getContent(Model model, HttpSession session,
                             @RequestParam(name = "meeting", required = false) int meetingId,
                             @RequestParam(name = "edit", required = false) Integer editMeeting) {

        // if user not registered
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }
        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
        model.addAttribute("user", registedUser);
        // get meeting object
        Meeting meetingObject = null;
        for (Meeting meeting : registedUser.getMeetings()) {
            int currentMeetingId = meeting.getId();
            if(currentMeetingId == meetingId){
                meetingObject = meeting;
            }
        }
        if(meetingObject == null)
            return new RedirectView("/user");


        // get meeting messages
        meetingObject.loadMessageComments();
        model.addAttribute("meeting", meetingObject);
        model.addAttribute("user", registedUser);
        model.addAttribute("editMeeting", editMeeting);

        return "chat.html";
    }
}
