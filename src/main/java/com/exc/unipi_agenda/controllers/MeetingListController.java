package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.Meeting;
import com.exc.unipi_agenda.model.User;
import com.exc.unipi_agenda.model.UserNotification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MeetingListController extends ContextController{
    @GetMapping(path = "/user")
    public Object getContent(Model model, HttpSession session,
                             @RequestParam(name = "open_meeting", required = false) Integer openMeeting) {

//        session.setAttribute("user", new User("prekwood"));
//        session.setAttribute("viewMode", "desktop");

        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }

        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));

        List<Object> notifications = UserNotification.loadNotifications(registedUser.getUsername());

        model.addAttribute("user", registedUser);
        model.addAttribute("notifications", notifications);

        if(this.isDesktop(session)){
            for (Meeting meeting:registedUser.getMeetings()) {
                meeting.loadMessageComments();
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
            model.addAttribute("open_meeting", openMeeting);
            return "meeting-list-desktop.html";
        }

        return "meeting-list.html";
    }

    @PostMapping("/logout")
    public Object logout(HttpSession session) {
        // if user not registered
        session.setAttribute("user", null);
        return new RedirectView("/");
    }
}
