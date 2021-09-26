package com.itransition.training.finalTask.Math.controller;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/exercises/{id}/rating/{num}")
    private String changeRating(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises id,
            @PathVariable int num) {

        if (num > 0 && num < 6)
            ratingService.changeRating(currentUser, id, num);
        return "redirect:/exercises/{id}";
    }

}
