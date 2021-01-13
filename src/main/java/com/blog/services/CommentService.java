package com.blog.services;

import com.blog.models.Comment;
import com.blog.models.User;
import com.blog.repositories.CommentRepository;
import com.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepo;

    public List<Comment> findAll() {
        return (List<Comment>) commentRepo.findAll();
    }

    public void deleteCommentById(int commentId) {
        commentRepo.deleteById(commentId);
    }

    public Comment getCommentById(int commentId) {
        List<Comment> comments = (List<Comment>) commentRepo.findAll();
        for (Comment comment : comments) {
            if (comment.getId() == commentId)
                return comment;
        }
        return null;
    }

    public String addComment(Comment newComment, String userName, int postId, Principal principal) {
        newComment.setPost(postService.getPostById(postId));
        newComment.setCreatedAt(new Date());
        if(principal!=null && userName.equals(principal.getName())) {
            newComment.setName(userRepo.findUserByUserName(userName).getName());
            newComment.setEmail(userRepo.findUserByUserName(userName).getEmail());
            newComment.setUser(userRepo.findUserByUserName(userName));
        }
        commentRepo.save(newComment);
        return "redirect:/view-post/{postId}";
    }

    public void updateComment(Comment comment, int postId) {
        Comment updatedComment = commentService.getCommentById(comment.getId());
        updatedComment.setName(comment.getName());
        updatedComment.setEmail(comment.getEmail());
        updatedComment.setMessage(comment.getMessage());
        updatedComment.setUpdatedAt(new Date());
        commentRepo.save(updatedComment);
    }

    public List<Comment> getCommentsByUser(User user) {
        return commentRepo.findCommentsByUser(user);
    }
}
