package com.itransition.training.finalTask.Math.services;

import com.itransition.training.finalTask.Math.models.Role;
import com.itransition.training.finalTask.Math.models.User;
import com.itransition.training.finalTask.Math.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return (UserDetails) user;
    }

    public void addUser(OAuth2User principal) {
        String id = principal.getName();
        userRepository.findById(id).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(id);
            newUser.setUsername(principal.getAttribute("name"));
            if (!newUser.getId().equals("86802154")) {
                newUser.setRoles(Collections.singleton(Role.USER));
            } else {
                newUser.setRoles(Collections.singleton(Role.ADMIN));
            }
            userRepository.save(newUser);
            return newUser;
        });
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(String id) {
        User user = getUser(id);
        userRepository.delete(user);
    }

    public void edit(String id) {
        User user = getUser(id);
        boolean active = user.isActive();

        if (active) user.setActive(false);
        else user.setActive(true);
        userRepository.save(user);

    }

    public void updateDesign(OAuth2User currentUser) {
        User user = getUser(currentUser);
        String design = user.getDesign().equals("standard") ? "black" : "standard";
        user.setDesign(design);
        userRepository.save(user);
    }

    public void updateLanguage(OAuth2User currentUser) {
        User user = getUser(currentUser);
        String language = user.getLanguage().equals("EN") ? "RU" : "EN";
        user.setLanguage(language);
        userRepository.save(user);
    }

    public User getUser(OAuth2User currentUser) {
        return userRepository.findById(currentUser.getName()).orElseThrow();
    }
    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User addTask(OAuth2User currentUser) {
        User user = getUser(currentUser);
        user.setTasksCreated(user.getTasksCreated() + 1);
        userRepository.save(user);

        return user;
    }

    public void removeTask(OAuth2User currentUser) {
        User user = getUser(currentUser);
        user.setTasksCreated(user.getTasksCreated() - 1);
        userRepository.save(user);
    }

    public boolean isAdmin(OAuth2User currentUser) {
        return getUser(currentUser).isAdmin();
    }
}
