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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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
        User user = userService.getUser(currentUser);
        model.addAttribute("design", user.getDesign());
        model.addAttribute("isAdmin", userService.isAdmin(currentUser));
        model.addAttribute("users", userService.findAll());
        return "adminPanel";
    }
    @GetMapping("/design")
    public String updateDesign(
            @AuthenticationPrincipal OAuth2User currentUser,
            RedirectAttributes attributes,
            @RequestHeader(required = false) String referer) {
        userService.updateDesign(currentUser);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
                .forEach(pair -> attributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }

    @GetMapping("/language")
    public String updateLanguage(
            @AuthenticationPrincipal OAuth2User currentUser,
            RedirectAttributes attributes,
            @RequestHeader(required = false) String referer) {
        userService.updateLanguage(currentUser);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
                .forEach(pair -> attributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
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
        model.addAttribute("username", user.getUsername());
        model.addAttribute("taskCreated", user.getTasksCreated());
        model.addAttribute("taskSolved", user.getTasksSolved());
        model.addAttribute("getId", user.getId());
        model.addAttribute("isCurrentUser", id.equals(currentUser.getName()));
        model.addAttribute("isAdmin", userService.isAdmin(currentUser));
        model.addAttribute("userId", currentUser.getName());
        model.addAttribute("design", userService.getUser(currentUser).getDesign());

        return "userExercises";
    }
}
