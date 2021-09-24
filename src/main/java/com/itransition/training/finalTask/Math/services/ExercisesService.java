package com.itransition.training.finalTask.Math.services;

import com.itransition.training.finalTask.Math.models.Exercises;
import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class ExercisesService {
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserService userService;

    public Iterable<Exercises> findAll() {
        return exerciseRepository.findAll();
    }

    public void updateExercise(OAuth2User currentUser, Exercises e, String name, String condition, String theme, String tags, String images, String rightAnswers) {
        if (e.getAuthor().getId().equals(currentUser.getName())) {

            if (name != null) {
                e.setName(name);
            }
            if (condition != null) {
                e.setCondition(condition);
            }
            if (theme != null) {
                e.setTheme(theme);
            }
            if (tags != null) {
                e.setTags(tags);
            }
            if (images != null) {
                e.setImages(images);
            }
            if (rightAnswers != null) {
                e.setRightAnswers(rightAnswers);
            }
            exerciseRepository.save(e);
        }
    }

    public void delete(OAuth2User currentUser, Long id) {
        userService.removeTask(currentUser);
        Exercises e = exerciseRepository.findById(id).orElseThrow();
        if (currentUser.getName().equals(e.getAuthor().getId()))
            exerciseRepository.delete(e);
    }

    public Long CreateExercises(String name, String condition, String theme, String tags, String images, String rightAnswers, OAuth2User currentUser) {
        User user = userService.addTask(currentUser);
        Exercises exercises = new Exercises(name, condition, theme, tags, images, rightAnswers, user);
        exerciseRepository.save(exercises);
        return exercises.getId();
    }

    public boolean exists(Long id) {
        if (exerciseRepository.existsById(id)) return true;
    return false;
    }

    public ArrayList<Exercises> detailExercise(Long id) {
        Optional<Exercises> exercises = exerciseRepository.findById(id);
        ArrayList<Exercises> res = new ArrayList<>();
        exercises.ifPresent(res::add);
        return res;
    }

    public void changeLikes(OAuth2User currentUser, Exercises exercise) {
        User user = userService.getUser(currentUser);
        if (getChecked(user, exercise.getLikes(), exercise.getDislikes(), exercise));
    }

    public void changeDislikes(OAuth2User currentUser, Exercises exercise) {
        User user = userService.getUser(currentUser);
        if (getChecked(user, exercise.getDislikes(), exercise.getLikes(), exercise));
    }

    private boolean getChecked(User user, Set<User> dislikes2, Set<User> likes2, Exercises exercise) {
        Set<User> likes = dislikes2;
        if (likes.contains(user)) {
            likes.remove(user);
            exerciseRepository.save(exercise);
            return true;
        } else {
            likes.add(user);
            Set<User> dislikes = likes2;
            if (dislikes.contains(user)) dislikes.remove(user);
        }
        exerciseRepository.save(exercise);
        return false;
    }

    public boolean isLiked(OAuth2User currentUser, Long id) {
        Exercises exercise = exerciseRepository.findById(id).orElseThrow();
        User user = userService.getUser(currentUser);
        Set<User> likes = exercise.getLikes();
        if (likes.contains(user)) return true;
        return false;
    }
    public boolean isDisliked(OAuth2User currentUser, Long id) {
        Exercises exercise = exerciseRepository.findById(id).orElseThrow();
        User user = userService.getUser(currentUser);
        Set<User> dislikes = exercise.getDislikes();
        if (dislikes.contains(user)) return true;
        return false;
    }
}
