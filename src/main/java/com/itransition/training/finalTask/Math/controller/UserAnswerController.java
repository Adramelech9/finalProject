package com.itransition.training.finalTask.Math.controller;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserAnswerController {
    @Autowired
    private UserAnswerService userAnswerService;

    @PostMapping("exercises/{id}/answer")
    public String setAnswer(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable("id") Exercises exercises,
            @RequestParam String answer) {
        userAnswerService.setUserAnswer(currentUser, exercises, answer);
        return "redirect:/exercises/{id}";
    }
}
