package com.itransition.training.finalTask.Math.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.itransition.training.finalTask.Math.drive.GoogleDriveUtils;
import com.itransition.training.finalTask.Math.model.Comment;
import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.Tags;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class ExercisesService {
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private UserAnswerService userAnswerService;
    @Autowired
    private CommentService commentService;

    public Iterable<Exercises> listAll(String keyword) {
        if (keyword != null) return exerciseRepository.search(keyword);
        return exerciseRepository.findAll();
    }

    public Iterable<Exercises> findAll2(Pageable pageable) {
        return exerciseRepository.findAll(pageable);
    }

    public void updateExercise(OAuth2User currentUser, Exercises e, String name,
                               String condition, String theme, String tags,
                               String images, String rightAnswers) {
        if (e.getAuthor().getId().equals(currentUser.getName()) || userService.isAdmin(currentUser)) {
            e.setName(name);
            e.setCondition(condition);
            e.setTheme(theme);
            e.setTags(tags);
            e.setImages(images);
            e.setRightAnswers(rightAnswers);
            exerciseRepository.save(e);
            tagsService.addTags(tags);
        }
    }

    public void delete(OAuth2User currentUser, Long id) {
        userService.removeTask(currentUser);
        if (currentUser.getName().equals(getExercises(id).getAuthor().getId())
                || userService.isAdmin(currentUser))
            exerciseRepository.delete(getExercises(id));
    }

    public Long CreateExercises(String name, String condition, String theme,
                                String tags, String images, String rightAnswers,
                                OAuth2User currentUser, String idUser) {
        User user;
        if (idUser == null) user = userService.getUser(currentUser);
        else if (!userService.getUser(currentUser).isAdmin())
            user = userService.addTask(currentUser);
        else user = userService.getUser(idUser);
        Exercises exercises = new Exercises(
                name, condition, theme, tags, images, rightAnswers, user);
        exerciseRepository.save(exercises);
        tagsService.addTags(tags);
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
        if (exercise.getLikes().add(user))
            exercise.getDislikes().remove(user);
        else exercise.getLikes().remove(user);
        exerciseRepository.save(exercise);
    }

    public void changeDislikes(OAuth2User currentUser, Exercises exercise) {
        User user = userService.getUser(currentUser);
        if (exercise.getDislikes().add(user))
            exercise.getLikes().remove(user);
        else exercise.getDislikes().remove(user);
        exerciseRepository.save(exercise);
    }

    public boolean isLiked(OAuth2User currentUser, Long id) {
        User user = userService.getUser(currentUser);
        if (getExercises(id).getLikes().contains(user)) return true;
        return false;
    }
    public boolean isDisliked(OAuth2User currentUser, Long id) {
        User user = userService.getUser(currentUser);
        if (getExercises(id).getDislikes().contains(user)) return true;
        return false;
    }

    public int likes(Long id) {
        return getExercises(id).getLikes().size();
    }
    public int dislikes(Long id) {
        return getExercises(id).getDislikes().size();
    }

    private Exercises getExercises(Long id) {
        return exerciseRepository.findById(id).orElseThrow();
    }

    public boolean isCurrentUser(OAuth2User currentUser, Long id) {
        User user = userService.getUser(currentUser);
        if (user.getId().equals(getExercises(id).getAuthor().getId())) return true;
        return false;
    }

    public double rating(Long id) {
        Exercises exercises = exerciseRepository.findById(id).orElseThrow();
        exercises.setRating(ratingService.averageRating(exercises));
        exerciseRepository.save(exercises);
        return ratingService.averageRating(exercises);
    }

    public boolean isVoted(OAuth2User currentUser, Long id) {
        Exercises exercises = exerciseRepository.findById(id).orElseThrow();
        return ratingService.isVoted(currentUser, exercises);
    }

    public Iterable<Tags> getTags() {
        return tagsService.getTags();
    }

    public boolean isRightAnswers(OAuth2User currentUser, Long id) {
        Exercises exercises = exerciseRepository.findById(id).orElseThrow();
        return userAnswerService.isRightAnswers(currentUser, exercises);
    }

    public String getAnswer(OAuth2User currentUser, Long id) {
        Exercises exercises = exerciseRepository.findById(id).orElseThrow();
        return userAnswerService.getUserAnswer(currentUser, exercises);
    }

    public Iterable<Comment> allComment(Long id) {
        Exercises exercises = exerciseRepository.findById(id).orElseThrow();
        return commentService.allComment(exercises);
    }

    public boolean isAdmin(OAuth2User currentUser) {
        return userService.isAdmin(currentUser);
    }

    public User user(OAuth2User currentUser) {
        return userService.getUser(currentUser);
    }

    public String transferImagesToString(MultipartFile images) throws IOException {
        String folderId = "1yuziQ0moxl_319JYs3AXfm2-9ulZYRUj";
        File fileMetadata = new File();
        fileMetadata.setName(images.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File(
                "/Users/denis/Desktop/" + images.getOriginalFilename());
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        Drive driveService = GoogleDriveUtils.getDriveService();

        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        return file.getId();
    }

    public String[] getArrImages(Long id) {
        Exercises exercises = exerciseRepository.findById(id).orElseThrow();
        return exercises.getImages().split(" ");
    }

    public String buildStringFile(@RequestParam MultipartFile images, @RequestParam MultipartFile images2, @RequestParam MultipartFile images3) throws IOException {
        String file = "";
        if (images != null && !images.getOriginalFilename().isEmpty()) {
            file += transferImagesToString(images);
            if (images2 != null && !images2.getOriginalFilename().isEmpty()) {
                file += " " + transferImagesToString(images2);
                if (images3 != null && !images3.getOriginalFilename().isEmpty())
                    file += " " + transferImagesToString(images3);
            }
        }
        return file;
    }
}
