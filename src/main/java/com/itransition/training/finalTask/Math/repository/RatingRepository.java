package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Exercises;
import com.itransition.training.finalTask.Math.model.Rating;
import com.itransition.training.finalTask.Math.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    List<Rating> findByIdExercisesAndIdUser(Exercises idExercises, User idUser);

    @Query("select r.sumRating from Rating r where r.idExercises=:idExercises")
    List<Double> findSumRatingByIdExercises(@Param("idExercises") Exercises idExercises);
}
