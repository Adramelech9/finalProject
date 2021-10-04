package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Comment;
import com.itransition.training.finalTask.Math.model.Exercises;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByIdCommentExercise(Exercises idExercises);
}
