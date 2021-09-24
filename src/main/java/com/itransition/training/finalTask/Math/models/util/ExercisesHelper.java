package com.itransition.training.finalTask.Math.models.util;

import com.itransition.training.finalTask.Math.models.User;

public class ExercisesHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }

}
