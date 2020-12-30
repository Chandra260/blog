package com.blog.Services;

import com.blog.Models.Comment;
import com.blog.Repositories.CommentRepository;
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

    public void createComment(String name, String email, String comment, int postId) {
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
        Comment editComment = findCommentByCommentId(commentId);
        editComment.setName(name);
        editComment.setEmail(email);
        editComment.setComment(comment);
        editComment.setUpdatedAt(postService.getTime());
        commentRepo.save(editComment);
    }
}
