package com.itransition.training.finalTask.Math.controller;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager em;

    @GetMapping("/delete/{id}")
    public String deleteUser(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable String id) {
        if (userService.isAdmin(currentUser)) userService.delete(id);
        return "redirect:/admin_panel";
    }

    @GetMapping("/update/{id}")
    public String editActivity(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable String id) {
        if (userService.isAdmin(currentUser)) userService.edit(id);
        return "redirect:/admin_panel";
    }

    @GetMapping("/admin_panel")
    public String adminPanel(
            @AuthenticationPrincipal OAuth2User currentUser,
            Model model) {
        if (!userService.isAdmin(currentUser)) return "redirect:/";
        model.addAttribute("isAdmin", userService.isAdmin(currentUser));
        model.addAttribute("users", userService.findAll());
        return "adminPanel";
    }
    @GetMapping("/design")
    public String updateDesign(@AuthenticationPrincipal OAuth2User currentUser) {
        userService.updateDesign(currentUser);
        return "redirect:/";
    }

    @GetMapping("/language")
    public String updateLanguage(@AuthenticationPrincipal OAuth2User currentUser) {
        userService.updateLanguage(currentUser);
        return "redirect:/";
    }

    @GetMapping("/office")
    public String goToPersonalOffice(@AuthenticationPrincipal OAuth2User currentUser) {
        User user = userService.getUser(currentUser);
        return "redirect:/office/" + user.getId();
    }

    @GetMapping("/office/{id}")
    public String personalOffice(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable String id,
            @RequestParam(required = false) Exercises exercise,
            Model model) {
        User user = userService.getUser(id);
        Set<Exercises> exercises = user.getExercises();
        model.addAttribute("exercises", exercises);
        model.addAttribute("exercise", exercise);
        model.addAttribute("user", user);
        model.addAttribute("isCurrentUser", id.equals(currentUser.getName()));
        model.addAttribute("isAdmin", userService.isAdmin(currentUser));
        model.addAttribute("userId", currentUser.getName());
        return "userExercises";
    }
}
