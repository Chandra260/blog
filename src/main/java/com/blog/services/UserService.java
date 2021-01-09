package com.blog.services;

import com.blog.models.MyUserDetails;
import com.blog.models.Post;
import com.blog.models.User;
import com.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(email);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new MyUserDetails(user);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        System.out.println(user);
        userRepo.save(user);
    }

    public List<Post> postsByUser(Principal principal) {
        List<Post> posts = userRepo.findAllPostsByUserEmail(principal.getName());
//        System.out.println(posts);
        return posts;
    }

    public List<Post> postsByUser(String email) {
        List<Post> posts = userRepo.findAllPostsByUserEmail(email);
        return posts;
    }
}
