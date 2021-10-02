package com.itransition.training.finalTask.Math.repository;

import com.itransition.training.finalTask.Math.model.Tags;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TagsRepository extends CrudRepository<Tags, Long> {
    Set<Tags> findByTag(String tag);
}
