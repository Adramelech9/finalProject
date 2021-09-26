package com.itransition.training.finalTask.Math.service;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.Rating;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserService userService;

    public void changeRating(OAuth2User currentUser, Exercises id, int num) {
        User user = userService.getUser(currentUser);
        if (ratingRepository.findByIdExercisesAndIdUser(id, user).size() == 0)
            ratingRepository.save(new Rating(id, user, num));
    }

    public double averageRating(Exercises id) {
        List<Double> ratings = ratingRepository.findSumRatingByIdExercises(id);
        if (ratings.size() == 0) return 0;
        double count = 0.0;
        for (int i = 0; i < ratings.size(); i++)
            count += Double.parseDouble(String.valueOf(ratings.get(i)));
        return count / ratings.size();
    }

    public boolean isVoted(OAuth2User currentUser, Exercises id) {
        User user = userService.getUser(currentUser);
        if (ratingRepository.findByIdExercisesAndIdUser(id, user).size() == 0)
            return false;
        return true;
    }
}
