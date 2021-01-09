package com.blog.repositories;

import com.blog.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("select distinct tag.name from Tag tag order by tag.name asc")
    public List<String> findDistinctByName();

}
