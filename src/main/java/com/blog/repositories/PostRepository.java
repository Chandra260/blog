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
            "(lower(post.title) like %?3% or lower(post.content) like %?3% or lower(post.author) like %?3% or lower(postTag.name) like %?3%)" +
            " and (post.author in ?1 and postTag.name in ?2 and post.publishedAt > ?4 )" +
            " order by post.publishedAt desc")
    public List<Post> findAllPublishedPosts(List author, List tags, String searchKeyword, Date dateTime);


//    @Query(nativeQuery = true, value="select * " +
//            "from posts,tags,posts_tags " +
//            "where posts.id = posts_tags.posts_id and tags.id = posts_tags.tags_id and " +
//            "(lower(posts.title) like %:searchKeyword% or " +
//            "lower(posts.content) like %:searchKeyword% or " +
//            "lower(posts.author) like %:searchKeyword% or " +
//            "lower(tags.name) like %:searchKeyword%)" +
//            " and (posts.author in :author and tags.name in :tags and posts.published_at > current_date - interval :dateTime)" +
//            " order by posts.published_at desc")
//public List<Post> findAllPublishedPosts(@Param("author") List author, @Param("tags") List tags, @Param("searchKeyword") String searchKeyword, @Param("dateTime") String dateTime);
//
//    @Query(nativeQuery = true, value="select * " +
//            "from posts,tags,posts_tags " +
//            "where posts.id = posts_tags.posts_id and tags.id = posts_tags.tags_id and " +
//            " posts.published_at > current_date - interval '1 days'")
//            " posts.published_at > :dateTime")
//    public List<Post> findAllPublishedPosts(@Param("dateTime") Date dateTime);

    @Query("select distinct author from Post order by author asc")
    public List<String> findDistinctByAuthor();

    @Query("select post from Post post where post.id = ?1")
    public Optional<Post> findPostById(int postId);

    @Query("select post from Post post where post.user = ?1")
    public List<Post> findPostsByUser(User user);

//    public Page<Post> findAll(Pageable pageable);

//    List<Post> findPaginated(int pageNo);
}
