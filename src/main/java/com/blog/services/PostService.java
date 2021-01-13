package com.blog.services;

import com.blog.models.Filter;
import com.blog.models.Post;
import com.blog.models.Tag;
import com.blog.models.User;
import com.blog.repositories.PostRepository;
import com.blog.repositories.TagRepository;
import com.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;
    @Autowired
    private TagRepository tagRepo;
    @Autowired
    private UserRepository userRepo;

    Filter filter = new Filter();

    public List<Post> getAllPosts() {
        return (List<Post>) postRepo.findAll();
    }

    public List<Post> getPublishedPosts() {
        List<Post> allPosts = getAllPosts();
        ArrayList<Post> publishedPosts = new ArrayList<Post>();
        for (Post post : allPosts) {
            if (post.isPublished()) {
                publishedPosts.add(post);
            }
        }
        filter.setHomePagePosts(publishedPosts);
        System.out.println("*"+filter);
        return publishedPosts;
    }

    public List<Post> getUnpublishedPosts() {
        List<Post> allPosts = postRepo.findAll();
        ArrayList<Post> unpublishedPosts = new ArrayList<Post>();
        for (Post post : allPosts) {
            if (!post.isPublished()) {
                unpublishedPosts.add(post);
            }
        }
        return unpublishedPosts;
    }

    public Post getPostById(int postId) {
        List<Post> posts = (List<Post>) postRepo.findAll();
        for (Post post : posts) {
            if (post.getId() == postId){
                return post;
            }
        }
        return null;
    }

    public void addPost(Post newPost, String tags, Principal principal) {
        List<Tag> tagsList = new ArrayList<>();
        String[] tagsArray = tags.split(",");
        for (String tag : tagsArray) {
            tagsList.add(tagRepo.findTagByName(tag));
        }
        newPost.setTags(tagsList);
        newPost.setExcerpt(newPost.getContent().substring(0, Math.min(newPost.getContent().length(), 50)));
        newPost.setCreatedAt(new Date());
        newPost.setPublished(false);
        if(newPost.getAuthor().equals(principal.getName())) {
            newPost.setAuthor(userRepo.findUserByUserName(principal.getName()).getName());
            newPost.setUser(userRepo.findUserByUserName(principal.getName()));
        } else {
            newPost.setAuthor(newPost.getAuthor());
        }
        postRepo.save(newPost);
    }

    public void publishPost(int postId) {
        Post post = getPostById(postId);
        post.setPublishedAt(new Date());
        post.setPublished(true);
        postRepo.save(post);
    }

    public void updatePost(Post post, String tags) {
        List<Tag> tagsList = new ArrayList<>();
        String[] tagsArray = tags.split(",");
        for (String tag : tagsArray) {
            tagsList.add(tagRepo.findTagByName(tag));
        }

        Post updatedPost = getPostById(post.getId());
        updatedPost.setTags(tagsList);
        updatedPost.setTitle(post.getTitle());
        updatedPost.setAuthor(post.getAuthor());
        updatedPost.setContent(post.getContent());
        updatedPost.setUpdatedAt(new Date());
        updatedPost.setExcerpt(updatedPost.getContent().substring(0, Math.min(updatedPost.getContent().length(), 50)));
        postRepo.save(updatedPost);
    }

    public void deletePostById(int id) {
        postRepo.deleteById(id);
    }

    public List<Post> getSortedPostsByPublishedDate() {
        List<Post> sortedPosts = filter.getHomePagePosts();
        Collections.sort(sortedPosts, new Comparator<Post>() {
            public int compare(Post post1, Post post2) {
                return post2.getPublishedAt().compareTo(post1.getPublishedAt());
            }
        });
        return sortedPosts;
    }

    public List<Post> getPostsBySearchKeyword(String searchKeyword) {
        List<Post> posts = postRepo.findAllBySearchedKeyword(searchKeyword);
        return postRepo.findAllBySearchedKeyword(searchKeyword);
    }

    public List<Post> getFilterBy(String author, String tags, Integer dateTime) {
        Set<Post> result = new HashSet<Post>();

        if(author==null && tags==null && dateTime==null) {
            return filter.getHomePagePosts();
        }

        else {

            if (author != null) {
                String[] authorsArray = author.split(",");
                List<Post> resultedPosts = postRepo.findAllByAuthor(Arrays.asList(authorsArray));
                result.addAll(filter.filteredPosts(resultedPosts));
            }

            if (tags != null) {
                String[] tagsArray = tags.split(",");
                List<Post> resultedPosts = postRepo.findAllByTags(Arrays.asList(tagsArray));
                result.addAll(filter.filteredPosts(resultedPosts));
            }

            if (dateTime != null) {
                filter.setDateTime(dateTime);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -filter.getDateTime() * 24);
                Date expiryDate = cal.getTime();
                System.out.println(expiryDate);

                List<Post> resultedPosts = postRepo.findAllByDate(expiryDate);

                result.addAll(filter.filteredPosts(resultedPosts));
            }

        }


        filter.setHomePagePosts(new ArrayList<Post>(result));
        return filter.getHomePagePosts();

    }

//    public List<Post> findPaginated(int pageNo) {
//        PageRequest paging = PageRequest.of(pageNo,3);
//        Page<Post> pagedResult = postRepo.findAll(paging);
//        return pagedResult.toList();
//    }

    public List<Post> getPostsByUser(User user) {
        return postRepo.findPostsByUser(user);
    }

}
