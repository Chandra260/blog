package com.blog.controllers;

import com.blog.models.Tag;
import com.blog.repositories.TagRepository;
import com.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepo;

    @RequestMapping("/create-tag")
    public String createTagForm(Model model) {
        model.addAttribute("newTag", new Tag());
        return "createTagForm";
    }

    @PostMapping("/create-tag")
    public String createNewTag(@ModelAttribute Tag newTag) {
        newTag.setCreatedAt(new Date());
        tagRepo.save(newTag);
        return "redirect:/";
    }
}
