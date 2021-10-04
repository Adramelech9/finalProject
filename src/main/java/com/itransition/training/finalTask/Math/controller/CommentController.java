package com.itransition.training.finalTask.Math.controller;

import com.itransition.training.finalTask.Math.model.Comment;
import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/exercises/{id}/comment")
    public String addComment(@AuthenticationPrincipal OAuth2User currentUser,
                             @PathVariable("id") Exercises exercises,
                             @RequestParam String comment) {
        commentService.addComment(currentUser, exercises, comment);
        return "redirect:/exercises/{id}";
    }

    @GetMapping("/exercises/{exercise}/like/{comment}")
    public String changeCommentLike(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises exercise,
            @PathVariable Comment comment) {
        commentService.changeCommentLikes(currentUser, comment);
        return "redirect:/exercises/{exercise}";
    }

    @GetMapping("/exercises/{exercise}/dislike/{comment}")
    public String changeCommentDislike(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises exercise,
            @PathVariable Comment comment) {
        commentService.changeCommentDislikes(currentUser, comment);
        return "redirect:/exercises/{exercise}";
    }

}
