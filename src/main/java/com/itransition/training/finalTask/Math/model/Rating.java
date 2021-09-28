package com.itransition.training.finalTask.Math.model;

import javax.persistence.*;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercises_id")
    private Exercises idExercises;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User idUser;
    private int sumRating;

    public Rating() {
    }

    public Rating(Exercises idExercises, User idUser, int sumRating) {
        this.idExercises = idExercises;
        this.idUser = idUser;
        this.sumRating = sumRating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercises getIdExercises() {
        return idExercises;
    }

    public void setIdExercises(Exercises idExercises) {
        this.idExercises = idExercises;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public int getSumRating() {
        return sumRating;
    }

    public void setSumRating(int sumRating) {
        this.sumRating = sumRating;
    }
}
