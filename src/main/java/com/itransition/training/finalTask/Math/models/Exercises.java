package com.itransition.training.finalTask.Math.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Exercises {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "The field cannot be empty")
    private String name, condition, theme, rightAnswers;
    private String tags, images;
    //private int sumRating = 0, numOfVotes = 0;
    //private int likes, dislikes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public Exercises() {
    }

    public Exercises(String name, String condition, String theme, String tags,
                     String images, String rightAnswers, User user) {
        this.name = name;
        this.condition = condition;
        this.theme = theme;
        this.tags = tags;
        this.images = images;
        this.rightAnswers = rightAnswers;
        this.author = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(String rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
