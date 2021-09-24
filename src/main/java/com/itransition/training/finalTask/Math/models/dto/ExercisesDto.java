package com.itransition.training.finalTask.Math.models.dto;

import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.models.Exercises;
import com.itransition.training.finalTask.Math.models.util.ExercisesHelper;

public class ExercisesDto {
    private Long id;
    private String name, condition, theme, rightAnswers;
    private String tags, images;
    private User author;
    private Long likes, dislikes;
    private boolean meLiked, meDisliked;

    public String getAuthorName() {
        return ExercisesHelper.getAuthorName(author);
    }

    public ExercisesDto(Exercises exercises, Long likes, Long dislikes, boolean meLiked, boolean meDisliked) {
        this.id = exercises.getId();
        this.name = exercises.getName();
        this.condition = exercises.getCondition();
        this.theme = exercises.getTheme();
        this.rightAnswers = exercises.getRightAnswers();
        this.tags = exercises.getTags();
        this.images = exercises.getImages();
        this.author = exercises.getAuthor();
        this.likes = likes;
        this.dislikes = dislikes;
        this.meLiked = meLiked;
        this.meDisliked = meDisliked;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    public String getTheme() {
        return theme;
    }

    public String getRightAnswers() {
        return rightAnswers;
    }

    public String getTags() {
        return tags;
    }

    public String getImages() {
        return images;
    }

    public User getAuthor() {
        return author;
    }

    public Long getLikes() {
        return likes;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public boolean isMeLiked() {
        return meLiked;
    }

    public boolean isMeDisliked() {
        return meDisliked;
    }

    @Override
    public String toString() {
        return "ExercisesDto{" +
                "id=" + id +
                ", author=" + author +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", meLiked=" + meLiked +
                ", meDisliked=" + meDisliked +
                '}';
    }
}
