package com.blog.controllers;

import com.blog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password) {
        userService.addUser(name, email, password);
        return "home";
    }

    @RequestMapping("/user-post")
    public String userPosts(Principal principal, Model model) {
        model.addAttribute("posts", userService.postsByUser(principal));
        return "userPost";
    }
}
