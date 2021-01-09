package com.blog.controllers;

import com.blog.models.User;
import com.blog.repositories.UserRepository;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController extends PostController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserRepository commentRepo;

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

    @RequestMapping("/{email}")
    public String userProfile(@PathVariable String email, Model model) {
        System.out.println(email);
        User user = userRepo.findByUserName(email);
        if(user==null) {
            return "errorPage";
        }
        model.addAttribute("user",userRepo.findByUserName(email));
        model.addAttribute("posts", userService.postsByUser(email));
        model.addAttribute("comments", userRepo.findAllCommentsByUserEmail(email));
        System.out.println(userRepo.findAllCommentsByUserEmail(email));
        return "userProfile";
    }

}
