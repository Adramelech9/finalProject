package com.itransition.training.finalTask.Math.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = {@JoinColumn(name = "exercise_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> commentLikes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "comment_dislikes",
            joinColumns = {@JoinColumn(name = "exercise_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> commentDislikes = new HashSet<>();

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

    public Set<User> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(Set<User> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public Set<User> getCommentDislikes() {
        return commentDislikes;
    }

    public void setCommentDislikes(Set<User> commentDislikes) {
        this.commentDislikes = commentDislikes;
    }
}
