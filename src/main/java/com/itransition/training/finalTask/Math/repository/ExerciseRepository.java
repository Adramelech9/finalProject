package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Exercises;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExerciseRepository extends CrudRepository<Exercises, Long> {

    Page<Exercises> findAll(Pageable pageable);

    @Query("select distinct e from Exercises e join Comment c on e.id = c.idCommentExercise where concat(" +
            "e.name, e.condition, e.tags, e.theme, e.author.username, c.text) " +
            "like %?1% ")
    List<Exercises> search(String keyword);
}
