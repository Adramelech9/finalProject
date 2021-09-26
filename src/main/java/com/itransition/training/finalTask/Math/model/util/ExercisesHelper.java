package com.itransition.training.finalTask.Math.model.util;

import com.itransition.training.finalTask.Math.model.User;

public class ExercisesHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }

}
