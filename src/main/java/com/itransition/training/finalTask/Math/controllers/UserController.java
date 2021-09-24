package com.itransition.training.finalTask.Math.controllers;

import com.itransition.training.finalTask.Math.models.Exercises;
import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.delete(id);
        return "redirect:/admin_panel";
    }

    @GetMapping("/update/{id}")
    public String editActivity(@PathVariable String id) {
        userService.edit(id);
        return "redirect:/admin_panel";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
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
        model.addAttribute("userId", currentUser.getName()); // or ADMIN
        return "userExercises";
    }
}
