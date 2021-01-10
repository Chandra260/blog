package com.blog.controllers;

import com.blog.models.Comment;
import com.blog.models.User;
import com.blog.repositories.CommentRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.CommentService;
import com.blog.services.PostService;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/view-post/{postId}")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    @PostMapping("/new-comment-form")
    public String createNewCommentForm(Model model, @PathVariable int postId) {
        model.addAttribute("postId", postId);
        model.addAttribute("newComment", new Comment());
        return "createCommentForm";
    }

    @PostMapping("/create-comment")
    public String newComment(@ModelAttribute Comment newComment, @Param("userName") String userName, @PathVariable int postId, Principal principal) {
        commentService.addComment(newComment, userName, postId, principal);
        return "redirect:/view-post/{postId}";
    }

    @RequestMapping("/edit-comment-form")
    public String editComment(@RequestParam("commentId") int commentId, Model model, @PathVariable int postId) {
        model.addAttribute("comment", commentService.getCommentById(commentId));
        return "editCommentForm";
    }

    @PostMapping("/update-comment")
    public String updateComment(@ModelAttribute Comment comment, @PathVariable int postId) {
        commentService.updateComment(comment, postId);
        return "redirect:/view-post/{postId}";
    }

    @PostMapping("/delete-comment")
    public String deleteComment(@PathVariable int postId, @RequestParam("commentId") int commentId) {
        commentService.deleteCommentById(commentId);
        return "redirect:/view-post/{postId}";
    }

    @PostMapping("/reply")
    public String replyComment(@RequestParam("commentId") int commentId, @PathVariable int postId, Model model) {
        model.addAttribute("commentId",commentId);
        model.addAttribute("newComment", new Comment());
        return "nestedComment";
    }

    @PostMapping("/nested-comment")
    public String nestedComment(@ModelAttribute Comment newComment, @RequestParam("commentId") int commentId, @PathVariable int postId,Principal principal, Model model) {
        Comment comment = commentRepo.findCommentById(commentId);
        User user = userService.getUserByUserName(principal.getName());
//        comment.setMessage(newComment.getMessage());
        newComment.setParentComment(comment);
        newComment.setName(user.getName());
        newComment.setEmail(user.getEmail());
        newComment.setPost(comment.getPost());
        newComment.setCreatedAt(commentService.getTime());
        newComment.setUser(user);
        commentRepo.save(newComment);
//        List<Comment> list = commentRepo.findByParentComment(comment);
//        System.out.println(list);
        comment.setChildComment(commentRepo.findByParentComment(comment));
        commentRepo.save(comment);
        return "redirect:/view-post/{postId}";
    }


}
