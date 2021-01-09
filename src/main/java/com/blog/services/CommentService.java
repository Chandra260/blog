package com.blog.services;

import com.blog.models.Comment;
import com.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private PostService postService;

    public List<Comment> findAll() {
        return (List<Comment>) commentRepo.findAll();
    }

    public void deleteCommentByPostId(int postId) {
        commentRepo.deleteById(postId);
    }

    public void addComment(String name, String email, String comment, int postId) {
        Comment newComment = new Comment();
        newComment.setName(name);
        newComment.setEmail(email);
        newComment.setComment(comment);
        newComment.setCreatedAt(postService.getTime());
        newComment.setPost(postService.findPostById(postId));
        commentRepo.save(newComment);
    }

    public Comment findCommentById(int postId) {
        List<Comment> comments = (List<Comment>) commentRepo.findAll();
        for (Comment comment : comments) {
            if (comment.getId() == postId)
                return comment;
        }
        return null;
    }

    public Comment findCommentByCommentId(int commentId) {
        List<Comment> comments = (List<Comment>) commentRepo.findAll();
        for (Comment comment : comments) {
            if (comment.getId() == commentId)
                return comment;
        }
        return null;
    }

    public void updateCommentByCommentId(int commentId, String name, String email, String comment) {
        Comment updateComment = findCommentByCommentId(commentId);
        updateComment.setName(name);
        updateComment.setEmail(email);
        updateComment.setComment(comment);
        updateComment.setUpdatedAt(postService.getTime());
        commentRepo.save(updateComment);
    }
}
