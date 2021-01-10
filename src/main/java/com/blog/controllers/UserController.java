package com.blog.controllers;

import com.blog.models.Comment;
import com.blog.models.User;
import com.blog.repositories.UserRepository;
import com.blog.services.CommentService;
import com.blog.services.PostService;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends PostController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserRepository commentRepo;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

//    @RequestMapping("/login")
//    public RedirectView login() {
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl("");
//    }

//    @RequestMapping("/{username}")
//    public RedirectView homePage(@PathVariable String username) {
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl("posts");
//        return redirectView;
//    }

    @RequestMapping("/{userName}")
    public String userProfile(@PathVariable String userName, Model model) {
        System.out.println(userName);
        User user = userService.getUserByUserName(userName);
//        model.addAttribute("userName", user.getUser
//        Name());
//        System.out.println(user);
//        if(user==null) {
//            return "errorPage";
//        }
        System.out.println(userService.getUserByUserName(userName));
        model.addAttribute("user",userService.getUserByUserName(userName));
        System.out.println(postService.getPostsByUser(user));
        model.addAttribute("posts", postService.getPostsByUser(user));
        System.out.println(commentService.getCommentsByUser(user));
        model.addAttribute("comments", commentService.getCommentsByUser(user));
//        System.out.println(userRepo.findAllCommentsByUserEmail(userName));
        return "userProfile";
    }



}
