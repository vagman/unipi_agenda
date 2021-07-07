package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class UserHomeController extends ContextController{
    @GetMapping(path = "/user")
    public Object getContent(Model model, HttpSession session) {

        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }
//        System.out.println(registedUser.getFullName());
        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));
        registedUser.setMeetingInvitations(refreshesInvitations(registedUser.getUsername()));
        return "agenda";
    }
}
