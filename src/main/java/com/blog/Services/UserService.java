package com.blog.Services;

import com.blog.Models.MyUserDetails;
import com.blog.Models.Post;
import com.blog.Models.User;
import com.blog.Repositories.UserRepository;
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
            throw new UsernameNotFoundException("Could not find user.");
        }
        return new MyUserDetails(user);
    }

    public void addUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepo.save(user);
    }

    public List<Post> postsByUser(Principal principal) {
        List<Post> posts = userRepo.findAllPostsByUserEmail(principal.getName());
        System.out.println(posts);
        return posts;
    }

}
