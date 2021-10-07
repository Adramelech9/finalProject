package com.itransition.training.finalTask.Math.service;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.model.UserAnswer;
import com.itransition.training.finalTask.Math.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserAnswerService {
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    @Autowired
    private UserService userService;

    public void setUserAnswer(OAuth2User currentUser, Exercises exercises, String answer) {
        User user = userService.getUser(currentUser);
        if (userAnswerRepository.findByIdExerciseAndIdUser(exercises, user).size() == 0) {
            userAnswerRepository.save(new UserAnswer(exercises, user, answer));
        } else {
            UserAnswer u = userAnswerRepository.findUserAnswerByIdExerciseAndIdUser(exercises, user);
            u.setAnswer(answer);
            userAnswerRepository.save(u);
        }
        if (isRightAnswers(currentUser, exercises, answer)) userService.addRightAnswers(user);
    }

    public String getUserAnswer(OAuth2User currentUser, Exercises exercises) {
        User user = userService.getUser(currentUser);
        UserAnswer u = userAnswerRepository.findUserAnswerByIdExerciseAndIdUser(exercises, user);
        if (u == null) return "your answer..";
        return userAnswerRepository.findUserAnswerByIdExerciseAndIdUser(exercises, user).getAnswer();
    }

    public boolean isRightAnswers(OAuth2User currentUser, Exercises exercises, String answer) {
        User user = userService.getUser(currentUser);
        UserAnswer u = userAnswerRepository.findUserAnswerByIdExerciseAndIdUser(exercises, user);
        if (u == null) return false;
        if (u.getAnswer().equals(answer)) return true;
        return false;
    }
}
