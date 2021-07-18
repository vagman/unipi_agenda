package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.Db;
import com.exc.unipi_agenda.model.Meeting;
import com.exc.unipi_agenda.model.Participant;
import com.exc.unipi_agenda.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MeetingController extends ContextController{

    @GetMapping("/meeting")
    public Object getContent(Model model, HttpSession session) {

        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }

        // Generate timepicker options
        List<String> timepickerOptions = new ArrayList<String>();
        for(int i=15; i<500; i+=15){
            String option = "";
            if((int)i/60 != 0){
                option += String.valueOf((int)i/60)+" hours ";
            }
            option += String.valueOf(i%60)+" min";

            timepickerOptions.add(option);
        }
        model.addAttribute("timepickerOptions", timepickerOptions);

        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
        model.addAttribute("user", registedUser);

        return "meeting";
    }

    @PostMapping("/create-meeting")
    public Object createMeeting(Model model,
                                HttpSession session,
                                @RequestParam(name = "meeting-title", required = true) String meetingTitle,
                                @RequestParam(name = "meeting-desc", required = true) String meetingDescription,
                                @RequestParam(name = "meeting-date", required = true) String meetingDateString,
                                @RequestParam(name = "meeting-duration", required = true) String meetingDuration,
                                @RequestParam(name = "meeting-participants", required = true) String meetingParticipants
    )
    {
        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }


        // Date formating
        Date meetingDate;
        try{
            meetingDate = new SimpleDateFormat("dd/MM/yyyy").parse(meetingDateString);
        }catch (Exception e){
            e.printStackTrace();
            meetingDate = new Date();
        }

        // Meeting creation
        Meeting newMeeting = new Meeting(meetingTitle,meetingDescription,meetingDate,meetingDuration);
        newMeeting.create(registedUser);

        // Participants
        String[] meetingParticipantsList = meetingParticipants.split("__separator__");
        newMeeting.getAdmin().addParticipants(newMeeting.getId(), meetingParticipantsList);

        // Rebuild the meetings list that is stored on the session
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));

        return new RedirectView("/user");
    }
    @PostMapping("/delete-meeting")
    public Object deleteMeeting(Model model,
                                HttpSession session,
                                @RequestParam(name = "id-meeting", required = true) int id_meeting,
                                @RequestParam(name = "isAdmin", required = true) boolean isAdmin)
    {
        User registedUser = (User)session.getAttribute("user");
        if (isAdmin) {
//            find the meeting
            for (Meeting m : registedUser.getMeetings()) {
                if (id_meeting == m.getId()) {
//                    Admin delete all the data from the meeting
                    m.getAdmin().delete(m.getId());
                    break;
                }
            }
        }else{
//            find the meeting
            for (Meeting m : registedUser.getMeetings()) {
                if (id_meeting == m.getId()) {
//                    find the user in the Participants
                    for (Participant p:m.getParticipants()){
                        if (registedUser.getUsername().equals(p.getUsername())){
//                          participant leave
                            p.leave(m.getId());
                            break;
                        }
                    }
                }
            }
        }
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        model.addAttribute("user", registedUser);
        return new RedirectView("/user");
    }

}
