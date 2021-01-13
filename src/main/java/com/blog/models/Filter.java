package com.blog.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Filter {

    private int dateTime;
    private List<Post> homePagePosts;

    public Filter() {
        dateTime = 365*5;
    }

    public int getDateTime() {
        return dateTime;
    }

    public void setDateTime(int dateTime) {
        this.dateTime = dateTime;
    }

    public List<Post> getHomePagePosts() {
        return homePagePosts;
    }

    public void setHomePagePosts(List<Post> homePagePosts) {
        this.homePagePosts = homePagePosts;
    }

    public List<Post> filteredPosts(List<Post> resultedPosts) {
        Set<Integer> resultedPostsId = new HashSet<Integer>();
        for (Post post : resultedPosts) {
            if (!resultedPostsId.contains(post.getId())) {
                resultedPostsId.add(post.getId());
            }
        }

        Set<Post> result = new HashSet<Post>();
        List<Post> posts = getHomePagePosts();
        for (Post post : posts) {
            for (Integer setId : resultedPostsId) {
                if (post.getId() == setId) {
                    result.add(post);
                }
            }
        }
        return new ArrayList<Post>(result);
    }

}
