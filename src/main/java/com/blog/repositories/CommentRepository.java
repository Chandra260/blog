package com.blog.repositories;

import com.blog.models.Comment;

import com.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select com from Comment com where com.id = ?1")
    public Comment findCommentById(int id);

    @Query("select com from Comment com where com.parentComment = ?1")
    public List<Comment> findByParentComment(Comment comment);

    @Query("select com from Comment com where com.user = ?1")
    public List<Comment> findCommentsByUser(User user);
}
