package com.blog.repositories;

import com.blog.models.Post;

import com.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
//    between now() - interval ?3 and now()

    @Query("select distinct post from Post post join post.tags postTag where post.isPublished=true and " +
            "lower(post.title) like %?1% or lower(post.content) like %?1% or lower(post.author) like %?1% or lower(postTag.name) like %?1%")
    public List<Post> findAllBySearchedKeyword(String searchKeyword);

    @Query("select distinct post from Post post where post.isPublished=true and post.author in ?1")
    public List<Post> findAllByAuthor(List author);

    @Query("select distinct post from Post post join post.tags postTag where post.isPublished=true and postTag.name in ?1")
    public List<Post> findAllByTags(List author);

    @Query("select distinct post from Post post where post.isPublished=true and post.publishedAt > ?1")
    public List<Post> findAllByDate(Date date);

    @Query("select distinct author from Post order by author asc")
    public List<String> findDistinctByAuthor();

    @Query("select post from Post post where post.user = ?1")
    public List<Post> findPostsByUser(User user);

//    public Page<Post> findAll(Pageable pageable);

//    List<Post> findPaginated(int pageNo);
}
