package com.exc.unipi_agenda.controllers;

import com.exc.unipi_agenda.model.User;
import com.exc.unipi_agenda.model.UserNotification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MeetingListController extends ContextController{
    @GetMapping(path = "/user")
    public Object getContent(Model model, HttpSession session) {

        User registedUser = (User)session.getAttribute("user");
        if(registedUser == null){
            return new RedirectView("/");
        }

        registedUser.setNotificationList(refreshesNotifications(registedUser.getUsername()));
        registedUser.setMeetings(refreshesMeetings(registedUser.getUsername()));

        List<Object> notifications = UserNotification.loadNotifications(registedUser.getUsername());

        model.addAttribute("user", registedUser);
        model.addAttribute("notifications", notifications);

        return "meeting-list.html";
    }

    @PostMapping("/logout")
    public Object logout(HttpSession session) {
        // if user not registered
        session.setAttribute("user", null);
        return new RedirectView("/");
    }
}
