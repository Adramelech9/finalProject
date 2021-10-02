package com.itransition.training.finalTask.Math.model;

import javax.persistence.*;

@Entity
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercises_id")
    private Exercises idExercise;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User idUser;
    private String answer;

    public UserAnswer() {
    }

    public UserAnswer(Exercises idExercise, User idUser, String answer) {
        this.idExercise = idExercise;
        this.idUser = idUser;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercises getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Exercises idExercise) {
        this.idExercise = idExercise;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
