package com.itransition.training.task04.Users.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
public class TableUsers {
    @Id
    private String id;
    @Column(updatable = false)
    private String userName, socialNetwork;
    private LocalDateTime firstEntry;
    private LocalDateTime lastEntry;
    private boolean isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(String socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public LocalDateTime getFirstEntry() {
        return firstEntry;
    }

    public void setFirstEntry(LocalDateTime firstEntry) {
        this.firstEntry = firstEntry;
    }

    public LocalDateTime getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(LocalDateTime lastEntry) {
        this.lastEntry = lastEntry;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public TableUsers() {
    }
}
