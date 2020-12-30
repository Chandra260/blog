package com.blog.Repositories;

import com.blog.Models.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select distinct post from Post post join post.tags postTag where post.isPublished=true and (lower(post.title) like %?4% or lower(post.content) like %?4% or lower(post.author) like %?4% or lower(postTag.name) like %?4%)" +
            " and (post.author in ?1 and postTag.name in ?2 or post.publishedAt in ?3)" +
            " order by post.publishedAt desc")
    public List<Post> findAllPublishedPosts(List author, List tags, List dateTime, String searchKeyword);

    @Query("select distinct author from Post order by author asc")
    public List<String> getDistinctByAuthor();


//    public Page<Post> findAll(Pageable pageable);

//    List<Post> findPaginated(int pageNo);
}
