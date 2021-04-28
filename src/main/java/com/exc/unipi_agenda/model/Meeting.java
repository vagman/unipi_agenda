package com.exc.unipi_agenda.model;

import java.util.Date;
import java.util.List;

public class Meeting {
    private Date datetime;
    private String name;
    private float duration;
    private User admin;
    private List<User> participants;
    private List<MeetingComment> comments;

    public void add(){}
    public void update(){}
    public void delete(){}
    public void addParticipants(List<User> participants){}
}
