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

        /*if (userFromDB != null) {
            tableUsers.setLastEntry(LocalDateTime.now());
            userRepository.save(tableUsers);
        }*/
        return "../static/index";
    }

    @GetMapping("/delete/{id}")
    public String editActive(@PathVariable(value = "id") long id, Model model) {
        TableUsers tableUsers = userRepository.findById(id).orElseThrow();
        userRepository.delete(tableUsers);
        return "redirect:/";
    }
}
