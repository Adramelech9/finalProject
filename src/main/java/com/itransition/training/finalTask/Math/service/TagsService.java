package com.itransition.training.finalTask.Math.service;

import com.itransition.training.finalTask.Math.model.Tags;
import com.itransition.training.finalTask.Math.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagsService {
    @Autowired
    private TagsRepository tagsRepository;

    public void addTags(String tags) {
        String[] splitTags = tags.toLowerCase().split(" ");
        for (int i = 0; i < splitTags.length; i++) {
            if (tagsRepository.findByTag(splitTags[i]).size() == 0) {
                Tags newTags = new Tags();
                newTags.setTag(splitTags[i]);
                tagsRepository.save(newTags);
            }
        }
    }

    public Iterable<Tags> getTags () {
        return tagsRepository.findAll();
    }
}
