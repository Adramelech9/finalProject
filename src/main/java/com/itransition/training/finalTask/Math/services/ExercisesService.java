package com.itransition.training.finalTask.Math.services;

import com.itransition.training.finalTask.Math.UsersApplication;
import com.itransition.training.finalTask.Math.models.Exercises;
import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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
        Exercises exercises = getExercises(id);
        if (currentUser.getName().equals(exercises.getAuthor().getId()))
            exerciseRepository.delete(exercises);
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
        Set<User> likes = exercise.getLikes();
        if (likes.contains(user)) likes.remove(user);
        else {
            likes.add(user);
            Set<User> dislikes = exercise.getDislikes();
            if (dislikes.contains(user)) dislikes.remove(user);
        }
        exerciseRepository.save(exercise);
    }

    public void changeDislikes(OAuth2User currentUser, Exercises exercise) {
        User user = userService.getUser(currentUser);
        Set<User> dislikes = exercise.getLikes();
        if (dislikes.contains(user)) dislikes.remove(user);
        else {
            dislikes.add(user);
            Set<User> likes = exercise.getDislikes();
            if (likes.contains(user)) likes.remove(user);
        }
        exerciseRepository.save(exercise);
    }

    public boolean isLiked(OAuth2User currentUser, Long id) {
        Exercises exercise = getExercises(id);
        User user = userService.getUser(currentUser);
        Set<User> likes = exercise.getLikes();
        if (likes.contains(user)) return true;
        return false;
    }
    public boolean isDisliked(OAuth2User currentUser, Long id) {
        Exercises exercise = getExercises(id);
        User user = userService.getUser(currentUser);
        Set<User> dislikes = exercise.getDislikes();
        if (dislikes.contains(user)) return true;
        return false;
    }

    public int likes(Long id) {
        Exercises exercises = getExercises(id);
        Set<User> likes = exercises.getLikes();
        return likes.size();
    }
    public int dislikes(Long id) {
        Exercises exercises = getExercises(id);
        Set<User> dislikes = exercises.getDislikes();
        return dislikes.size();
    }

    private Exercises getExercises(Long id) {
        return exerciseRepository.findById(id).orElseThrow();
    }

    public boolean isCurrentUser(OAuth2User currentUser, Long id) {
        User user = userService.getUser(currentUser);
        Exercises exercises = getExercises(id);
        if (user.getId().equals(exercises.getAuthor().getId())) return true;
        return false;
    }
}
