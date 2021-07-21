package com.exc.unipi_agenda.controllers;


import com.exc.unipi_agenda.model.*;
import org.hibernate.Session;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContextController {

    public List<Object> refreshesNotifications(String username){
        return UserNotification.loadNotifications(username);
    }

    public List<Meeting> refreshesMeetings(String username){
        return Meeting.loadMeetings(username);
    }

    public boolean isDesktop(HttpSession session){
        String viewMode = (String)session.getAttribute("viewMode");
        if(viewMode != null && viewMode.equals("desktop")){
            return true;
        }
        return false;
    }
}
