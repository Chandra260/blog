package com.blog.controllers;

import com.blog.Services.CommentService;
import com.blog.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestParam("postId") int postId) {
        commentService.deleteCommentByPostId(postId);
        return "home";
    }

    @PostMapping("/new-comment")
    public String createComment(@RequestParam("postId") int postId, Model model) {
        model.addAttribute("postId", postId);
        return "comment";
    }

    @PostMapping("/create-comment")
    public String newComment(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("comment") String comment, @RequestParam("postId") int postId) {
        commentService.createComment(name, email, comment, postId);
        return "home";
    }

    @GetMapping("/edit-comment")
    public String editPost(@RequestParam("commentId") int commentId, Model model) {
        model.addAttribute("comment", commentService.findCommentByCommentId(commentId));
        return "editComment";
    }

    @PostMapping("/update-comment")
    public String updatePost(@RequestParam("commentId") int commentId, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("comment") String comment) {
        commentService.updateCommentByCommentId(commentId, name, email, comment);
        return "home";
    }
}
