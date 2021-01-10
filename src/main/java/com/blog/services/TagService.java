package com.blog.services;

import com.blog.models.Tag;
import com.blog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TagService {

    @Autowired
    TagRepository tagRepo;

    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Tag addTag(String tags) {
        Tag tag = new Tag();
        tag.setName(tags);
        tag.setCreatedAt(getTime());
        tagRepo.save(tag);
        return tag;
    }

    public Tag findTagByName(String tags) {
        List<Tag> ListOfTags = tagRepo.findAll();
        for (Tag tag : ListOfTags) {
            if (tag.getName().equals(tags)) {
                return tag;
            }
        }
        return null;
    }
}
