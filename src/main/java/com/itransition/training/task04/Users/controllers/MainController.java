package com.itransition.training.task04.Users.controllers;

import com.itransition.training.task04.Users.models.TableUsers;
import com.itransition.training.task04.Users.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping ("/")
    public String controlPanel(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("countUsers", userRepository.count());
        model.addAttribute("countByFacebook", userRepository.countBySocialNetwork("facebook"));
        model.addAttribute("countByGoogle", userRepository.countBySocialNetwork("google"));
        model.addAttribute("countByGithub", userRepository.countBySocialNetwork("github"));


        return "redirect:/#_=_";
    }

    @GetMapping("/delete/{id}")
    public String editActive(@PathVariable(value = "id") String id) {
        TableUsers tableUsers = userRepository.findById(id).orElseThrow();
        userRepository.delete(tableUsers);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateActivity(@PathVariable(value = "id") String id) {
        TableUsers tableUsers = userRepository.findById(id).orElseThrow();
        boolean active = tableUsers.isActive();

        if (active) tableUsers.setActive(false);
        else tableUsers.setActive(true);
        userRepository.save(tableUsers);
        return "redirect:/";
    }
}
