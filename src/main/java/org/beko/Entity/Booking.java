package org.beko.Entity;

import java.time.LocalDateTime;

public class Booking {
    private String id;
    private User user;
    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Booking(String id, User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.user = user;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "(Id: " + id + ") " + user + ", place=" + place +
                ", startTime=" + startTime +
                ", endTime=" + endTime;
    }
}
