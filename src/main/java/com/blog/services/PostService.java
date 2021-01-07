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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;
    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepo;
    @Autowired
    private UserRepository userRepo;

    Filter filter = new Filter();

    public List<Post> findAllPosts() {
        return (List<Post>) postRepo.findAll();
    }

    public List<Post> findPublishedPosts() {
        filter.setAuthor(String.join(",", postRepo.getDistinctByAuthor()));
        filter.setTags(String.join(",", tagRepo.findDistinctByName()));
        List<Post> allPosts = findAllPosts();
        ArrayList<Post> publishedPosts = new ArrayList<Post>();
        for (Post post : allPosts) {
            if (post.isPublished()) {
                publishedPosts.add(post);
            }
        }
        return publishedPosts;
    }

    public List<Post> findUnpublishedPosts() {
        List<Post> allPosts = findAllPosts();
        ArrayList<Post> unpublishedPosts = new ArrayList<Post>();
        for (Post post : allPosts) {
            if (!post.isPublished()) {
                unpublishedPosts.add(post);
            }
        }
        return unpublishedPosts;
    }

    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Post findPostById(int postId) {
        List<Post> posts = (List<Post>) postRepo.findAll();
        for (Post post : posts) {
            if (post.getId() == postId)
                return post;
        }
        return null;
    }

    public void createPost(String title, String author, String tags, String content) {
        Post post = new Post();
        List<Tag> listOfTags = new ArrayList<>();
        String[] tagsArray = tags.split(",");
        for (int tag = 0; tag < tagsArray.length; tag++) {
            listOfTags.add(tagService.findTagByName(tagsArray[tag]));
        }
        if (author.endsWith("@gmail.com")) {
            User user = userRepo.findByUserName(author);
            post.setUser(user);
            post.setAuthor(user.getName());
        } else {
            post.setAuthor(author);
        }

        post.setTags(listOfTags);
        post.setTitle(title);
        post.setExcerpt(content.substring(0, 10));
        post.setContent(content);
        post.setCreatedAt(getTime());
        post.setPublished(false);
        postRepo.save(post);
    }

    public void publishPost(int postId) {
        Post post = findPostById(postId);
        post.setPublishedAt(getTime());
        post.setPublished(true);
        postRepo.save(post);
    }

    public void updatePostById(int postId, String title, String author, String tags, String content) {
        Post post = findPostById(postId);
        List<Tag> listOfTags = new ArrayList<>();
        String[] tagsArray = tags.split(",");
        for (int tag = 0; tag < tagsArray.length; tag++) {
            listOfTags.add(tagService.findTagByName(tagsArray[tag]));
        }
        post.setTags(listOfTags);
        post.setTitle(title);
        post.setExcerpt(content.substring(0, 10));
        post.setContent(content);
        post.setAuthor(author);
        post.setUpdatedAt(getTime());
        postRepo.save(post);
    }

    public void deletePostById(int id) {
        postRepo.deleteById(id);
    }

    public List<Post> getSortedPostsByPublishedDate() {
        List<Post> sortedPosts = findPublishedPosts();
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

    public List<Post> findFilterBy(String author, String tags, String dateTime) {
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
}
