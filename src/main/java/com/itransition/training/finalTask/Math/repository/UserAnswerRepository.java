package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.User;
import com.itransition.training.finalTask.Math.model.UserAnswer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAnswerRepository extends CrudRepository<UserAnswer, Long> {
    List<UserAnswer> findByIdExerciseAndIdUser(Exercises idExercise, User idUser);

    UserAnswer findUserAnswerByIdExerciseAndIdUser(Exercises idExercise, User idUser);

}
