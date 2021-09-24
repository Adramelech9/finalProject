package com.itransition.training.finalTask.Math.controllers;

import com.itransition.training.finalTask.Math.models.Exercises;
import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.repository.ExerciseRepository;
import com.itransition.training.finalTask.Math.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Controller
public class MainController {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public MainController(UserRepository userRepository,
                          ExerciseRepository exerciseRepository) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping ("/")
    public String authPage(Model model) {
        model.addAttribute("exercises", exerciseRepository.findAll());
        return "../static/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/admin_panel";
    }

    @GetMapping("/update/{id}")
    public String editActivity(@PathVariable String id) {
        User user = userRepository.findById(id).orElseThrow();
        boolean active = user.isActive();

        if (active) user.setActive(false);
        else user.setActive(true);
        userRepository.save(user);
        return "redirect:/admin_panel";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "adminPanel";
    }
    @GetMapping("/design")
    public String updateDesign(@AuthenticationPrincipal OAuth2User currentUser) {
        User user = userRepository.findById(currentUser.getName()).orElseThrow();
        String design = user.getDesign().equals("standard") ? "black" : "standard";
        user.setDesign(design);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/language")
    public String updateLanguage(@AuthenticationPrincipal OAuth2User currentUser) {
        User user = userRepository.findById(currentUser.getName()).orElseThrow();
        String language = user.getLanguage().equals("EN") ? "RU" : "EN";
        user.setLanguage(language);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/exercises")
    public String exercises(
            @AuthenticationPrincipal OAuth2User currentUser,
            Model model) {
        model.addAttribute("exercises", exerciseRepository.findAll());
        model.addAttribute("userId", currentUser.getName());
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
        User user = userRepository.findById(currentUser.getName()).orElseThrow();
        Exercises exercises = new Exercises(name, condition, theme, tags, images, rightAnswers, user);
        user.setTasksCreated(user.getTasksCreated() + 1);
        exerciseRepository.save(exercises);
        userRepository.save(user);
        model.addAttribute("exercises", exerciseRepository.findAll());
        return "redirect:/exercises/" + exercises.getId();
    }

    @GetMapping("/office")
    public String goToPersonalOffice(@AuthenticationPrincipal OAuth2User currentUser) {
        User user = userRepository.findById(currentUser.getName()).orElseThrow();
        return "redirect:/office/" + user.getId();
    }

    @GetMapping("/office/{id}")
    public String personalOffice(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable String id,
            @RequestParam(required = false) Exercises exercise,
            Model model) {
        User user = userRepository.findById(id).orElseThrow();
        Set<Exercises> exercises = user.getExercises();
        model.addAttribute("exercises", exercises);
        model.addAttribute("exercise", exercise);
        model.addAttribute("user", user);
        model.addAttribute("isCurrentUser", id.equals(currentUser.getName()));
        model.addAttribute("userId", currentUser.getName()); // or ADMIN
        return "userExercises";
    }

    @PostMapping("/office/{user}")
    public String updateExercise(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Long user,
            @RequestParam("exercise") Exercises e,
            @RequestParam String name,
            @RequestParam String condition,
            @RequestParam String theme,
            @RequestParam String tags,
            @RequestParam String images,
            @RequestParam String rightAnswers) {
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
        return "redirect:/office/" + user;
    }

    @GetMapping("/exercises/{id}/delete")
    public String deleteExercise(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Long id) {
        Exercises e = exerciseRepository.findById(id).orElseThrow();
        if (currentUser.getName().equals(e.getAuthor().getId()))
            exerciseRepository.delete(e);
        return "redirect:/office";
    }

    @GetMapping("/exercises/{id}")
    public String detailExercise(@PathVariable Long id, Model model) {
        if (!exerciseRepository.existsById(id)) return "redirect:/exercises";
        Optional<Exercises> exercises = exerciseRepository.findById(id);
        ArrayList<Exercises> res = new ArrayList<>();
        exercises.ifPresent(res::add);
        model.addAttribute("exercises", res);
        return "theExercise";
    }
}
