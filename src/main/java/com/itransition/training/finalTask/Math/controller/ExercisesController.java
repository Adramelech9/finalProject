package com.itransition.training.finalTask.Math.controller;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.service.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

@Controller
public class ExercisesController {
    @Autowired
    private ExercisesService exercisesService;

    @GetMapping("/")
    public String authPage(Model model) {
        model.addAttribute("exercises", exercisesService.findAll());
        model.addAttribute("tags", exercisesService.getTags());
        return "../static/index";
    }

    @GetMapping("/exercises")
    public String exercises(
            @AuthenticationPrincipal OAuth2User currentUser,
            Model model) {
        model.addAttribute("exercises", exercisesService.findAll());
        model.addAttribute("userId", currentUser.getName());
        model.addAttribute("tags", exercisesService.getTags());
        model.addAttribute("isAdmin", exercisesService.isAdmin(currentUser));
        return "exercisesMain";
    }

    @PostMapping("/exercises")
    public String CreateExercises(
            @AuthenticationPrincipal OAuth2User currentUser,
            @RequestParam String name,
            @RequestParam String condition,
            @RequestParam String theme,
            @RequestParam String tags,
            @RequestParam String images,
            @RequestParam String rightAnswers,
            Model model) {
        Long id = exercisesService.CreateExercises(name, condition, theme, tags, images, rightAnswers, currentUser);
        model.addAttribute("exercises", exercisesService.findAll());
        return "redirect:/exercises/" + id;
    }

    @PostMapping("/office/{id}")
    public String updateExercise(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable String id,
            @RequestParam("exercise") Exercises e,
            @RequestParam String name,
            @RequestParam String condition,
            @RequestParam String theme,
            @RequestParam String tags,
            @RequestParam String images,
            @RequestParam String rightAnswers) {
        exercisesService.updateExercise(currentUser, e, name, condition, theme, tags, images, rightAnswers);
        return "redirect:/office/" + id;
    }

    @GetMapping("/exercises/{id}/delete")
    public String deleteExercise(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Long id) {
        exercisesService.delete(currentUser, id);
        return "redirect:/office";
    }

    @GetMapping("/exercises/{id}")
    public String detailExercise(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Long id,
            Model model) {
        if (!exercisesService.exists(id)) return "redirect:/exercises";
        ArrayList<Exercises> res = exercisesService.detailExercise(id);
        model.addAttribute("exercises", res);
        model.addAttribute("likes", exercisesService.likes(id));
        model.addAttribute("rating", exercisesService.rating(id));
        model.addAttribute("dislikes", exercisesService.dislikes(id));
        model.addAttribute("comment", exercisesService.allComment(id));
        if (currentUser != null) {
            model.addAttribute("like", exercisesService.isLiked(currentUser, id));
            model.addAttribute("isVoted", exercisesService.isVoted(currentUser, id));
            model.addAttribute("dislike", exercisesService.isDisliked(currentUser, id));
            model.addAttribute("isCurrentUser", exercisesService.isCurrentUser(currentUser, id));
            model.addAttribute("read_only", false);
            model.addAttribute("answer", exercisesService.getAnswer(currentUser, id));
            model.addAttribute("isRightAnswers", exercisesService.isRightAnswers(currentUser, id));
            model.addAttribute("isAdmin", exercisesService.isAdmin(currentUser));
        } else {
            model.addAttribute("isVoted", true);
            model.addAttribute("read_only", true);
            model.addAttribute("answer", "");
            model.addAttribute("isRightAnswers", true);
        }
        return "theExercise";
    }

    @GetMapping("/exercises/{exercise}/like")
    public String changeLikes(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises exercise,
            RedirectAttributes attributes,
            @RequestHeader(required = false) String referer) {
        exercisesService.changeLikes(currentUser, exercise);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
            .forEach(pair -> attributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }

    @GetMapping("/exercises/{exercise}/dislike")
    public String changeDislikes(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises exercise,
            RedirectAttributes attributes,
            @RequestHeader(required = false) String referer) {
        exercisesService.changeDislikes(currentUser, exercise);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
                .forEach(pair -> attributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }
}
