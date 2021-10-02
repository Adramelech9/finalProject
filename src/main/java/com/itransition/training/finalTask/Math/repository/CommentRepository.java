package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Comment;
import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByIdCommentExerciseAndAndIdCommentUser(Exercises idExercises, User idUser);
    List<Comment> findByIdCommentExercise(Exercises idExercises);
}
