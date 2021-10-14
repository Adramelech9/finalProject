package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Exercises;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<Exercises, Long> {

    Page<Exercises> findAll(Pageable pageable);

    Page<Exercises> findByTags(String tags, Pageable pageable);
}
