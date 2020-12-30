package com.blog.Repositories;

import com.blog.Models.Post;
import com.blog.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select user from User user where user.email = ?1")
    public User findByUserName(String email);

    @Query("select post from Post post join post.user postUser where postUser.email = ?1")
    public List<Post> findAllPostsByUserEmail(String email);

}
