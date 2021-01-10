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
        filter.setAuthor(String.join(",", postRepo.findDistinctByAuthor()));
        filter.setTags(String.join(",", tagRepo.findDistinctByName()));
        List<Post> allPosts = getAllPosts();
        ArrayList<Post> publishedPosts = new ArrayList<Post>();
        for (Post post : allPosts) {
            if (post.isPublished()) {
                publishedPosts.add(post);
            }
        }
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

    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Post getPostById(int postId) {
        List<Post> posts = (List<Post>) postRepo.findAll();
        for (Post post : posts) {
            if (post.getId() == postId)
                return post;
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
        newPost.setCreatedAt(getTime());
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
        post.setPublishedAt(getTime());
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
//        Optional<Post> postDb = postRepo.findPostById(post.getId());
//        Post updatedPost = postDb.get();
        System.out.println(updatedPost);
        updatedPost.setTags(tagsList);
        updatedPost.setTitle(post.getTitle());
        updatedPost.setAuthor(post.getAuthor());
        updatedPost.setContent(post.getContent());
        updatedPost.setUpdatedAt(getTime());
        updatedPost.setExcerpt(updatedPost.getContent().substring(0, Math.min(updatedPost.getContent().length(), 50)));
        postRepo.save(updatedPost);
    }

    public void deletePostById(int id) {
        postRepo.deleteById(id);
    }

    public List<Post> getSortedPostsByPublishedDate() {
        List<Post> sortedPosts = getPublishedPosts();
        Collections.sort(sortedPosts, new Comparator<Post>() {
            public int compare(Post post1, Post post2) {
                return post2.getPublishedAt().compareTo(post1.getPublishedAt());
            }
        });
        return sortedPosts;
    }

    public List<Post> getPostsBySearchKeyword(String searchKeyword) {
        filter.setSearch(searchKeyword);
        String[] authorsArray = filter.getAuthor().split(",");
        String[] tagsArray = filter.getTags().split(",");
        String[] dateTimeArray = filter.getDateTime().split(",");
        return postRepo.findAllPublishedPosts(Arrays.asList(authorsArray), Arrays.asList(tagsArray), Arrays.asList(dateTimeArray), filter.getSearch());
    }

    public List<Post> getFilterBy(String author, String tags, String dateTime) {
        if (author != null) {
            filter.initializeAuthor();
        }
        if (tags != null) {
            filter.initializeTags();
        }

        filter.setAuthor(filter.getAuthor() + "," + author);
        filter.setTags(filter.getTags() + "," + tags);
        filter.setDateTime(filter.getDateTime() + "," + dateTime);

        String[] authorsArray = filter.getAuthor().split(",");
        String[] tagsArray = filter.getTags().split(",");
        String[] dateTimeArray = filter.getDateTime().split(",");
        return postRepo.findAllPublishedPosts(Arrays.asList(authorsArray), Arrays.asList(tagsArray), Arrays.asList(dateTimeArray), filter.getSearch());
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
