package com.blog.controllers;

import com.blog.models.Post;
import com.blog.models.User;
import com.blog.repositories.PostRepository;
import com.blog.repositories.TagRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.PostService;
import com.blog.services.TagService;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private TagRepository tagRepo;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserRepository userRepo;
    
//    @RequestMapping("/")
//    public ModelAndView redirect()
//    {
//        String URL = "https://www.youtube.com/";
//        return new ModelAndView("redirect:"+URL);//File Disclosure: Spring  vulnerability
//    }

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("posts", postService.getPublishedPosts());
        model.addAttribute("authors", postRepo.findDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

    @RequestMapping("/create-post-form")
    public String create(Model model) {
        model.addAttribute("post", new Post());
        return "createPostForm";
    }

    @PostMapping("/create-post")
    public String createNewPost(@ModelAttribute Post post, @RequestParam("tag") String tags, Principal principal) {
        postService.addPost(post, tags, principal);
        return "redirect:/";
    }

    @GetMapping("/publish-post")
    public String publish(Model model) {
        model.addAttribute("posts", postService.getUnpublishedPosts());
        return "publishPost";
    }

    @PostMapping("/publish-post/{postId}")
    public String publishPost(@PathVariable int postId) {
        postService.publishPost(postId);
        return "redirect:/publish-post";
    }

    @RequestMapping("/view-post/{postId}")
    public String viewPost(@PathVariable int postId, Model model) {
        model.addAttribute("post", postService.getPostById(postId));
        return "viewPost";
    }

    @GetMapping("/edit-post-form")
    public String editPost(@RequestParam("postId") int postId, Model model) {
        model.addAttribute("post", postService.getPostById(postId));
        return "editPostForm";
    }

    @PostMapping("/update-post")
    public String updatePost(@ModelAttribute Post post, @RequestParam("tag") String tags) {
        postService.updatePost(post, tags);
        return "redirect:/";
    }

    @PostMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable int postId) {
        postService.deletePostById(postId);
        return "redirect:/";
    }

    @GetMapping("/sort")
    public String sortByPublishedTimeDate(Model model) {
        model.addAttribute("posts", postService.getSortedPostsByPublishedDate());
        model.addAttribute("authors", postRepo.findDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

    @RequestMapping("/search-post")
    public String searchPosts(@RequestParam("search") String searchKeyword, Model model) {
        model.addAttribute("posts", postService.getPostsBySearchKeyword(searchKeyword.toLowerCase()));
        model.addAttribute("authors", postRepo.findDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

    @RequestMapping("/filter")
    public String filterByUserKeywords(@Param("author") String author, @Param("tags") String tags, @Param("dateTime") String dateTime, Model model) {
        model.addAttribute("posts", postService.getFilterBy(author, tags, dateTime));
        model.addAttribute("authors", postRepo.findDistinctByAuthor());
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
        model.addAttribute("newUser", new User());
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
    public String registerUser(@ModelAttribute User newUser, Model model) {
        User user = userRepo.findUserByUserName(newUser.getUserName());
        if(user==null) {
            userService.addUser(newUser);
            return "redirect:/";
        } else {
            model.addAttribute(user);
            model.addAttribute("newUser", newUser);
            model.addAttribute("message", "Username already exists !!");
            return "register_error";
        }
    }


    @RequestMapping("/my-post")
    public String userPosts(Principal principal, Model model) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("posts", postService.getPostsByUser(user));
        return "myPost";
    }
}
