package com.exc.unipi_agenda.model;

import org.springframework.ui.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class UserNotification {
    public UserNotification(int idNotification, String message, Date datetime, boolean viewed){
        this.idNotification = idNotification;
        this.message = message;
        this.datetime = datetime;
        this.viewed = viewed;
    }

    public int getIdNotification() {
        return idNotification;
    }

    //private User user;
    private final int idNotification;
    private final String message;
    private final Date datetime;

    public String getMessage() {
        return message;
    }

    public Date getDatetime() {
        return datetime;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    private boolean viewed;

    public boolean markAsViewed(String idNotification, Model model){
        Connection conn = Db.getConnection();
        if (conn != null) {
            String sql_query = "UPDATE user_notification SET viewed=true WHERE id_notification =?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql_query);
                ps.setString(1,idNotification);
                return ps.execute();
            }catch (SQLException throwables) {
                model.addAttribute("error","Something wrong with database");
                throwables.printStackTrace();
            }
        }
        return false;
    }
}


// User::login,regiter
// User info change
// Metting list (home)
// Meeting info
// Metting create/edit (popup)
//

