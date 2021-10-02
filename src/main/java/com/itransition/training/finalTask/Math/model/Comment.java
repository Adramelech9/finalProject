package com.itransition.training.finalTask.Math.model;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercises_id")
    private Exercises idCommentExercise;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User idCommentUser;

    public Comment() {
    }

    public Comment(Exercises idExercise, User idUser, String text) {
        this.text = text;
        this.idCommentExercise = idExercise;
        this.idCommentUser = idUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Exercises getIdCommentExercise() {
        return idCommentExercise;
    }

    public void setIdCommentExercise(Exercises idCommentExercise) {
        this.idCommentExercise = idCommentExercise;
    }

    public User getIdCommentUser() {
        return idCommentUser;
    }

    public void setIdCommentUser(User idCommentUser) {
        this.idCommentUser = idCommentUser;
    }
}
