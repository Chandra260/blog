package com.blog.repositories;

import com.blog.models.Post;

import com.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select distinct post from Post post join post.tags postTag where post.isPublished=true and (lower(post.title) like %?4% or lower(post.content) like %?4% or lower(post.author) like %?4% or lower(postTag.name) like %?4%)" +
            " and (post.author in ?1 or postTag.name in ?2 or post.publishedAt in ?3)" +
            " order by post.publishedAt desc")
    public List<Post> findAllPublishedPosts(List author, List tags, List dateTime, String searchKeyword);

    @Query("select distinct author from Post order by author asc")
    public List<String> findDistinctByAuthor();

    @Query("select post from Post post where post.id = ?1")
    public Optional<Post> findPostById(int postId);

    @Query("select post from Post post where post.user = ?1")
    public List<Post> findPostsByUser(User user);

//    public Page<Post> findAll(Pageable pageable);

//    List<Post> findPaginated(int pageNo);
}
