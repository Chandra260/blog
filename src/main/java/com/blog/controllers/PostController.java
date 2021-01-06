package com.blog.controllers;

import com.blog.Repositories.PostRepository;
import com.blog.Repositories.TagRepository;
import com.blog.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private TagRepository tagRepo;

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("posts", postService.findPublishedPosts());
        model.addAttribute("authors", postRepo.getDistinctByAuthor());
        model.addAttribute("tags", tagRepo.findDistinctByName());
        return "home";
    }

    @RequestMapping("/create-post")
    public String create() {
        return "createPost";
    }

    @PostMapping("/create-post")
    public String createPost(@RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("tags") String tags, @RequestParam("content") String content) {
        postService.createPost(title, author, tags, content);
        return "home";
    }

    @GetMapping("/publish-post")
    public String publish(Model model) {
        model.addAttribute("posts", postService.findUnpublishedPosts());
        return "publishPost";
    }

    @PostMapping("/publish-post")
    public String publishPost(@RequestParam("postId") int postId) {
        postService.publishPost(postId);
        return "home";
    }

    @PostMapping("/update-post")
    public String updatePost(@RequestParam("postId") int postId, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("tags") String tags, @RequestParam("content") String content) {
        postService.updatePostById(postId, title, author, tags, content);
        return "home";
    }

    @PostMapping("/view-post")
    public String viewPost(@RequestParam("postId") int postId, Model model) {
        model.addAttribute("post", postService.findPostById(postId));
        return "viewPost";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam("postId") int postId) {
        postService.deletePostById(postId);
        return "home";
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

}
