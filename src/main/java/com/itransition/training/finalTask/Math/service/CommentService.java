package com.itransition.training.finalTask.Math.service;

import com.itransition.training.finalTask.Math.model.Comment;
import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    public void addComment(OAuth2User currentUser, Exercises exercises, String text) {
        User user = userService.getUser(currentUser);
        commentRepository.save(new Comment(exercises, user, text));
    }

    public Iterable<Comment> allComment(Exercises exercises) {
        return commentRepository.findByIdCommentExercise(exercises);
    }
}
