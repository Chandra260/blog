package com.blog.controllers;

import com.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;
}
