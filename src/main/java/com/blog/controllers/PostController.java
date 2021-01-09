package com.blog.controllers;

import com.blog.models.User;
import com.blog.repositories.PostRepository;
import com.blog.repositories.TagRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.PostService;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private TagRepository tagRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("posts", postService.findPublishedPosts());
        model.addAttribute("authors", postRepo.getDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
//        model.addAttribute("user",userRepo.findByUserName());
        return "home";
    }

    @RequestMapping("/create-post")
    public String create() {
        return "createPost";
    }

    @PostMapping("/create-post")
    public String createPost(@RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("tags") String tags, @RequestParam("content") String content) {
        postService.createPost(title, author, tags, content);
        return "redirect:/";
    }

    @GetMapping("/publish-post")
    public String publish(Model model) {
        model.addAttribute("posts", postService.findUnpublishedPosts());
        return "publishPost";
    }

    @PostMapping("/publish-post/{postId}")
    public String publishPost(@PathVariable int postId) {
//        System.out.println(postId);
        postService.publishPost(postId);
        return "redirect:/publish-post";
    }

    @PostMapping("/update-post")
    public String updatePost(@RequestParam int postId, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("tags") String tags, @RequestParam("content") String content) {
        postService.updatePostById(postId, title, author, tags, content);
        return "redirect:/";
    }

    @RequestMapping("/view-post/{postId}")
    public String viewPost(@PathVariable int postId, Model model) {
        model.addAttribute("post", postService.findPostById(postId));
        return "viewPost";
    }

    @PostMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable int postId) {
        postService.deletePostById(postId);
        return "redirect:/";
    }

    @GetMapping("/edit-post")
    public String editPost(@RequestParam("postId") int postId, Model model) {
        model.addAttribute("post", postService.findPostById(postId));
        return "editPost";
    }

    @GetMapping("/sort")
    public String sortByPublishedTimeDate(Model model) {
        model.addAttribute("posts", postService.getSortedPostsByPublishedDate());
        model.addAttribute("authors", postRepo.getDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

    @RequestMapping("/search-post")
    public String searchPosts(@RequestParam("search") String searchKeyword, Model model) {
        model.addAttribute("posts", postService.getPostsBySearchKeyword(searchKeyword));
        model.addAttribute("authors", postRepo.getDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

    @RequestMapping("/filter")
    public String filterByUserKeywords(@Param("author") String author, @Param("tags") String tags, @Param("dateTime") String dateTime, Model model) {
        model.addAttribute("posts", postService.findFilterBy(author, tags, dateTime));
        model.addAttribute("authors", postRepo.getDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

//    @GetMapping("/{pageNo}")
//    public String getPaginated(@PathVariable int pageNo, Model model) {
//        model.addAttribute("posts", postService.findPaginated(pageNo));
//        model.addAttribute("authors", postRepo.getDistinctByAuthor());
//        model.addAttribute("tags", tagRepo.findDistinctByName());
//        return "home";
//    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute(new User());
        return "register";
    }

//    @PostMapping("/register")
//    public String registerUser(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password) {
//        User user = userRepo.findByUserName(email);
//        if(user==null) {
//            userService.addUser(name, email, password);
//            return "home";
//        } else {
////            return "redirect:/register";
//            return "errorPage";
//        }
//    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        System.out.println(user);
        User u = userRepo.findByUserName(user.getEmail());
        if(u==null) {
            userService.addUser(user);
            return "redirect:/";
        } else {
            model.addAttribute(u);
            model.addAttribute("message", "Email is already registered !!");
            return "register_error";
        }
    }


    @RequestMapping("/my-post")
    public String userPosts(Principal principal, Model model) {
        model.addAttribute("posts", userService.postsByUser(principal));
        return "myPost";
    }
}
