package com.exc.unipi_agenda.model;

import java.util.Date;
import java.util.List;

public class Meeting {
    private final String id;
    private String name;
    private Date datetime;
    private float duration;
    private final String admin;
    private List<User> participants;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getAdmin() {
        return admin;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<MeetingComment> getComments() {
        return comments;
    }

    public void setComments(List<MeetingComment> comments) {
        this.comments = comments;
    }

    private List<MeetingComment> comments;

    public Meeting(String id, String admin) {
        this.id = id;
        this.admin = admin;
    }

    public void add(){}
    public void update(){}
    public void delete(){}
    public void addParticipants(List<User> participants){}
}
