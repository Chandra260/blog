package com.blog.controllers;

import com.blog.models.Comment;
import com.blog.repositories.CommentRepository;
import com.blog.services.CommentService;
import com.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/delete-comment")
    public String deleteComment(@PathVariable int postId, @RequestParam("commentId") int commentId) {
        commentService.deleteCommentByPostId(commentId);
        return "redirect:/view-post/{postId}";
    }

    @PostMapping("/new-comment")
    public String createComment(Model model, @PathVariable int postId) {
        model.addAttribute("postId", postId);
        return "comment";
    }

    @PostMapping("/create-comment")
    public String newComment(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("comment") String comment, @PathVariable int postId) {
        commentService.addComment(name, email, comment, postId);
        return "redirect:/view-post/{postId}";
    }

    @GetMapping("/edit-comment")
    public String editComment(@RequestParam("commentId") int commentId, Model model, @PathVariable int postId) {
        model.addAttribute("comment", commentService.findCommentByCommentId(commentId));
        return "editComment";
    }

    @PostMapping("/update-comment")
    public String updateComment(@RequestParam("commentId") int commentId, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("comment") String comment, @PathVariable int postId) {
        commentService.updateCommentByCommentId(commentId, name, email, comment);
        return "redirect:/view-post/{postId}";
    }

    @PostMapping("/reply")
    public String replyComment(@RequestParam("commentId") int commentId, @PathVariable int postId, Model model) {
        model.addAttribute("commentId",commentId);
        return "nestedComment";
    }

    @PostMapping("/nested-comment")
    public String nestedComment(@RequestParam("commentId") int commentId, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("comment") String message, @PathVariable int postId, Model model) {
        Comment nestedComment = new Comment();
        Comment comment = commentRepo.findById(commentId);
        nestedComment.setParentComment(comment);
        nestedComment.setName(name);
        nestedComment.setEmail(email);
        nestedComment.setComment(message);
        nestedComment.setPost(comment.getPost());
        nestedComment.setCreatedAt(postService.getTime());
        Comment savedComment = commentRepo.save(nestedComment);
        List<Comment> list = commentRepo.findByParentComment(comment);
        System.out.println(list);
        comment.setChildComment(commentRepo.findByParentComment(comment));
        commentRepo.save(comment);
        return "redirect:/view-post/{postId}";
    }


}
