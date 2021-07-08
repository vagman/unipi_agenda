package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.Meeting;
import com.exc.unipi_agenda.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class MeetingController extends ContextController{

    @GetMapping("/meeting")
    public Object getContent(Model model, HttpSession session) {

        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }
        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
        model.addAttribute("user", registedUser);

        return "meeting";
    }

    @PostMapping("/meeting")
    public Object createMeeting(Model model,
                                HttpSession session,
                                @RequestParam(name = "meeting-title", required = false) String meetingTitle,
                                @RequestParam(name = "meeting-desc", required = false) String meetingDescription,
                                @RequestParam(name = "meeting-date", required = false) String meetingDate,
                                @RequestParam(name = "meeting-duration", required = false) String meetingDuration,
                                @RequestParam(name = "meeting-participants", required = false) String meetingParticipants
    )
    {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }

        Meeting newMeeting = new Meeting();
        newMeeting.setName(meetingTitle);
//        newMeeting.setDatetime(meetingDate);
//        newMeeting.set(meetingDate);

        String[] meetingParticipantsList = meetingParticipants.split("__separator__");
        newMeeting.getAdmin().addParticipants(newMeeting.getId(), meetingParticipantsList, model);


//        String date_string = "2100-09-26 10:00:00";
//        //Instantiating the SimpleDateFormat class
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        //Parsing the given String to Date object
//        Date date = formatter.parse(date_string);
//
//        a.createMeeting("meeting test 132", date_string, 12.5f,m);
        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
        session.setAttribute("user",registedUser);
        model.addAttribute("user", registedUser);
        return "index";
    }
}
