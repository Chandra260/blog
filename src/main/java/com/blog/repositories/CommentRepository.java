package com.blog.repositories;

import com.blog.models.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select com from Comment com where com.id = ?1")
    public Comment findById(int id);

}