package com.itransition.training.finalTask.Math.controller;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.service.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class ExercisesController {
    @Autowired
    private ExercisesService exercisesService;

    @GetMapping("/")
    public String authPage(
            Model model,
            @PageableDefault(sort = {"id"},
                    direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Exercises> page = (Page<Exercises>) exercisesService.findAll2(pageable);
        int[] sizeList = {5, 10, 15, 20};
        int[] body;
        if (page.getTotalPages() > 7) {
            int totalPages = page.getTotalPages();
            int pageNumber = page.getNumber()+1;
            int[] head = (pageNumber > 4) ? new int[]{1, -1} : new int[]{1,2,3};
            int[] bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1) ? new int[]{pageNumber-2, pageNumber-1} : new int[]{};
            int[] bodyCenter = (pageNumber > 3 && pageNumber < totalPages - 2) ? new int[]{pageNumber} : new int[]{};
            int[] bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3) ? new int[]{pageNumber+1, pageNumber+2} : new int[]{};
            int[] tail = (pageNumber < totalPages - 3) ? new int[]{-1, totalPages} : new int[] {totalPages-2, totalPages-1, totalPages};
            body = ControllerUtils.merge(head, bodyBefore, bodyCenter, bodyAfter, tail);

        } else {
            body = new int[page.getTotalPages()];
            for (int i = 0; i < page.getTotalPages(); i++) {
                body[i] = 1+i;
            }
        }

        model.addAttribute("sizeList", sizeList);
        model.addAttribute("body", body);
        model.addAttribute("page", page);
        model.addAttribute("url", "/");
        model.addAttribute("tags", exercisesService.getTags());
        return "../static/index";
    }

    @GetMapping("/exercises")
    public String exercises(
            @AuthenticationPrincipal OAuth2User currentUser,
            @Param("keyword") String keyword,
            Model model) {
        User user = exercisesService.user(currentUser);
        model.addAttribute("design", user.getDesign());
        model.addAttribute("exercises", exercisesService.listAll(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("userId", currentUser.getName());
        model.addAttribute("tags", exercisesService.getTags());
        model.addAttribute("isAdmin", exercisesService.isAdmin(currentUser));
        return "exercisesMain";
    }

    @PostMapping("/office/{idUser}/creat")
    public String CreateExercises(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable(required = false) String idUser,
            @RequestParam String name,
            @RequestParam String condition,
            @RequestParam String theme,
            @RequestParam String tags,
            @RequestParam String images,
            @RequestParam String rightAnswers) {
        Long id = exercisesService.CreateExercises(
                name, condition, theme, tags, images,
                rightAnswers, currentUser, idUser);
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
            model.addAttribute("user", exercisesService.user(currentUser));
            model.addAttribute("design", exercisesService.user(currentUser).getDesign());
        } else {
            model.addAttribute("isVoted", true);
            model.addAttribute("read_only", true);
            model.addAttribute("design", "standard");

        }
        return "theExercise";
    }

    @GetMapping("/exercises/{exercise}/like")
    public String changeLikes(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises exercise) {
        exercisesService.changeLikes(currentUser, exercise);
        return "redirect:/exercises/{exercise}";
    }

    @GetMapping("/exercises/{exercise}/dislike")
    public String changeDislikes(
            @AuthenticationPrincipal OAuth2User currentUser,
            @PathVariable Exercises exercise) {
        exercisesService.changeDislikes(currentUser, exercise);
        return "redirect:/exercises/{exercise}";
    }
}
