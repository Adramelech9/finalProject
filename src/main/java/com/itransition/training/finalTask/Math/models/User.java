package com.itransition.training.finalTask.Math.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String username, language, design;
    private boolean isActive;
    private int tasksCreated, tasksSolved;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "likes")
    private Set<Exercises> likes = new HashSet<>();

    @ManyToMany(mappedBy = "dislikes")
    private Set<Exercises> dislikes = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Exercises> exercises;

    public Set<Role> getRoles() {
        return roles;
    }

    /*public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public User() {
        this.language = "EN";
        this.design = "standard";
        this.isActive = true;
        this.tasksCreated = 0;
        this.tasksSolved = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public Set<Exercises> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercises> exercises) {
        this.exercises = exercises;
    }

    public int getTasksCreated() {
        return tasksCreated;
    }

    public void setTasksCreated(int tasksCreated) {
        this.tasksCreated = tasksCreated;
    }

    public int getTasksSolved() {
        return tasksSolved;
    }

    public void setTasksSolved(int tasksSolved) {
        this.tasksSolved = tasksSolved;
    }

    public Set<Exercises> getLikes() {
        return likes;
    }

    public void setLikes(Set<Exercises> likes) {
        this.likes = likes;
    }

    public Set<Exercises> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<Exercises> dislikes) {
        this.dislikes = dislikes;
    }
}
